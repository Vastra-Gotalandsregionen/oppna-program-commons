package se.vgregion.messagebus;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusException;
import com.liferay.portal.kernel.messaging.sender.DefaultSynchronousMessageSender;
import com.liferay.portal.kernel.uuid.PortalUUID;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.security.SslSocketConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * User: pabe
 * Date: 2011-05-10
 * Time: 16:12
 */
@ContextConfiguration(locations = {"/META-INF/camelRestComponentTest.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CamelRestComponentTest {

    @Autowired
    private MessageBus messageBus;

    @Value("${messagebus.rest.destination}")
    String messagebusDestination;
    @Value("${messagebus.unresponsive.rest.destination}")
    String messagebusUnresponsiveDestination;

    private Server server = new Server();

    private StringBuilder expected;

    @Before
    public void setUp() throws Exception {
        //Pick up the <http:conduit> configuration
        SpringBusFactory bf = new SpringBusFactory();
        Bus bus = bf.createBus("META-INF/camelRestComponentTest.xml");
        bf.setDefaultBus(bus);

        configureSslSocketConnector();

        server.addHandler(new AbstractHandler() {
            @Override
            public void handle(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
                expected = new StringBuilder();
                System.out.println(httpServletRequest);
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
        server.start();
    }

    private void configureSslSocketConnector() {
        SslSocketConnector sslSocketConnector = new SslSocketConnector();
        sslSocketConnector.setPort(8008);
        sslSocketConnector.setNeedClientAuth(true);

        String serverKeystore = this.getClass().getClassLoader().getResource("cert/serverkeystore.jks").getPath();
        sslSocketConnector.setKeystore(serverKeystore);
        sslSocketConnector.setKeyPassword("serverpass");
        String serverTruststore = this.getClass().getClassLoader().getResource("cert/servertruststore.jks").getPath();
        sslSocketConnector.setTruststore(serverTruststore);
        sslSocketConnector.setTrustPassword("serverpass");
        server.addConnector(sslSocketConnector);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    @DirtiesContext
    public void testMessageBusToRestAndBackToMessageBus() throws MessageBusException {
        Message message = new Message();
        message.setPayload("test");

        DefaultSynchronousMessageSender sender = new DefaultSynchronousMessageSender();
        sender.setPortalUUID(new PortalUUID() {
            @Override
            public String fromJsSafeUuid(String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String generate() {
                Random random = new Random();
                return random.nextInt() + "";
            }

            @Override
            public String toJsSafeUuid(String s) {
                throw new UnsupportedOperationException();
            }
        });
        sender.setMessageBus(messageBus);
        Object result = sender.send(messagebusDestination, message, 15000);
        assertEquals(expected.toString(), result);
    }

    @Test
    @DirtiesContext
    public void testMessageBusToRestTimeout() throws MessageBusException {
        Message message = new Message();
        message.setPayload("test");

        DefaultSynchronousMessageSender sender = new DefaultSynchronousMessageSender();
        sender.setPortalUUID(new PortalUUID() {
            @Override
            public String fromJsSafeUuid(String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String generate() {
                Random random = new Random();
                return random.nextInt() + "";
            }

            @Override
            public String toJsSafeUuid(String s) {
                throw new UnsupportedOperationException();
            }
        });
        sender.setMessageBus(messageBus);
        try {
            sender.send(messagebusDestination, message, 1);
            fail();
        } catch (Exception ex) {
            assertTrue(ex.getMessage().startsWith("No reply received for message"));
        }
    }

    @Test
    @DirtiesContext
    public void testMessageBusToUnresponsiveRestAndBackToMessageBus() throws MessageBusException {
        Message message = new Message();
        message.setPayload("test");

        DefaultSynchronousMessageSender sender = new DefaultSynchronousMessageSender();
        sender.setPortalUUID(new PortalUUID() {
            @Override
            public String fromJsSafeUuid(String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String generate() {
                Random random = new Random();
                return random.nextInt() + "";
            }

            @Override
            public String toJsSafeUuid(String s) {
                throw new UnsupportedOperationException();
            }
        });
        sender.setMessageBus(messageBus);
        Object result = sender.send(messagebusUnresponsiveDestination, message, 15000);
        assertTrue(result instanceof Exception);
    }
}
