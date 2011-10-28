package se.vgregion.messagebus;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusException;
import com.liferay.portal.kernel.messaging.sender.DefaultSynchronousMessageSender;
import com.liferay.portal.kernel.uuid.PortalUUID;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.helpers.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.SslSocketConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.http.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: pabe
 * Date: 2011-05-10
 * Time: 16:12
 */
@ContextConfiguration(locations = {"/META-INF/camelHttpComponentTest.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CamelHttpComponentTest {

    @Autowired
    private MessageBus messageBus;

    //looks up the property via Spring's PropertyPlaceholder
    @Value("${messagebus.http.destination}")
    String messagebusDestination;

    private Server server = new Server();

    //Use fields to verify what happens in the embedded test server.
    private StringBuilder expected;
    private String queryString;
    private String body;

    @Before
    public void setUp() throws Exception {

        configureSslSocketConnector();

        SecurityHandler securityHandler = createBasicAuthenticationSecurityHandler();

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(securityHandler);
        handlerList.addHandler(new AbstractHandler() {
            @Override
            public void handle(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
                expected = new StringBuilder();

                System.out.println("uri: " + httpServletRequest.getRequestURI());
                System.out.println("queryString: " + (queryString = httpServletRequest.getQueryString()));
                System.out.println("method: " + httpServletRequest.getMethod());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(httpServletRequest.getInputStream(), baos);

                System.out.println("body: " + (body = baos.toString()));

                PrintWriter writer = httpServletResponse.getWriter();
                writer.append("testsvar");
                expected.append("testsvar");
                Random r = new Random();
                for (int j = 0; j < 10; j++) {
                    int value = r.nextInt(Integer.MAX_VALUE);
                    writer.append(value + "");
                    expected.append(value);
                }
                System.out.println();
                writer.close();
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            }
        });

        server.addHandler(handlerList);

        server.start();
    }

    private SecurityHandler createBasicAuthenticationSecurityHandler() {
        Constraint constraint = new Constraint(Constraint.__BASIC_AUTH, "superuser");
        constraint.setAuthenticate(true);

        HashUserRealm myRealm = new HashUserRealm("MyRealm");
        myRealm.put("supername", "superpassword");
        myRealm.addUserToRole("supername", "superuser");

        SecurityHandler securityHandler = new SecurityHandler();
        securityHandler.setUserRealm(myRealm);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");
        securityHandler.setConstraintMappings(new ConstraintMapping[]{constraintMapping});
        return securityHandler;
    }

    private void configureSslSocketConnector() {
        //we set up two ports to make it easy to test both with and without encryption

        SocketConnector socketConnector = new SocketConnector();
        socketConnector.setPort(6080);

        SslSocketConnector sslSocketConnector = new SslSocketConnector();
        sslSocketConnector.setPort(6443);
        sslSocketConnector.setNeedClientAuth(true); //why not use mutual authentication when we can

        String serverKeystore = this.getClass().getClassLoader().getResource("cert/serverkeystore.jks").getPath();
        sslSocketConnector.setKeystore(serverKeystore);
        sslSocketConnector.setKeyPassword("serverpass");
        String serverTruststore = this.getClass().getClassLoader().getResource("cert/servertruststore.jks").getPath();
        sslSocketConnector.setTruststore(serverTruststore);
        sslSocketConnector.setTrustPassword("serverpass");

        server.addConnector(socketConnector);
        server.addConnector(sslSocketConnector);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    @DirtiesContext
    public void testMessageBusToHttpAndBackToMessageBus() throws MessageBusException {
        Message message = new Message();
        Map<String, String> params = new HashMap();
        params.put("myParam", "myValue");
        params.put("myParam2", "myValue2");
        //see se.vgregion.messagebus.EndpointMessageListener.createExchange() to see how the payload object is handled
        //a map will be translated to an http request body in a POST request
        message.setPayload(params);

        DefaultSynchronousMessageSender sender = createMessageSender();

        Object result = sender.send(messagebusDestination, message, 15000);
        assertEquals(expected.toString(), result);
        //the ordering is not deterministic and doesn't matter so both alternatives are ok
        assertTrue( "myParam=myValue&myParam2=myValue2".equals(body) ||
                    "myParam2=myValue2&myParam=myValue".equals(body));
    }

    @Test
    @DirtiesContext
    public void testWithHttpRequestWithQueryByString() throws MessageBusException {

        HttpRequest httpRequest = new HttpRequest();
        String localQueryString = "OpenAgent&username=ex_teste&password=123456";
        httpRequest.setQueryByString(localQueryString);

        Message message = new Message();
        //see se.vgregion.messagebus.EndpointMessageListener.createExchange() to see how the payload object is handled
        message.setPayload(httpRequest);

        DefaultSynchronousMessageSender sender = createMessageSender();

        Object result = sender.send(messagebusDestination, message, 15000);

        assertEquals(localQueryString, queryString);
    }

    @Test
    @DirtiesContext
    public void testWithHttpRequestWithQueryMap() throws MessageBusException {

        HttpRequest httpRequest = new HttpRequest();
        Map<String, String> params = new HashMap();
        params.put("myParam", "myValue");
        params.put("myParam2", "myValue2");
        httpRequest.setQueryByMap(params);

        Message message = new Message();
        //see se.vgregion.messagebus.EndpointMessageListener.createExchange() to see how the payload object is handled
        message.setPayload(httpRequest);

        DefaultSynchronousMessageSender sender = createMessageSender();

        Object result = sender.send(messagebusDestination, message, 15000);

        //the ordering is not deterministic and doesn't matter so both alternatives are ok
        assertTrue( "myParam=myValue&myParam2=myValue2".equals(queryString) ||
                    "myParam2=myValue2&myParam=myValue".equals(queryString));
    }

    private DefaultSynchronousMessageSender createMessageSender() {
        //just to make a working sender without having a real Liferay server running
        DefaultSynchronousMessageSender sender = new DefaultSynchronousMessageSender();
        sender.setPortalUUID(new PortalUUID() {
            @Override
            public String generate() {
                Random random = new Random();
                return random.nextInt() + "";
            }
        });
        sender.setMessageBus(messageBus);
        return sender;
    }

}
