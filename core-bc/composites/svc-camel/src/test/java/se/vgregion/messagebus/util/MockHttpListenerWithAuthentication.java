package se.vgregion.messagebus.util;

import org.apache.cxf.helpers.IOUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.SslSocketConnector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * User: pabe
 * Date: 2011-05-23
 * Time: 16:59
 */
public class MockHttpListenerWithAuthentication {

    private static Server server = new Server();

    public static void main(String[] args) throws Exception {
        SocketConnector socketConnector = new SocketConnector();
        socketConnector.setPort(6080);

        SslSocketConnector sslSocketConnector = new SslSocketConnector();
        sslSocketConnector.setPort(6443);
//        sslSocketConnector.setNeedClientAuth(true); //why not use mutual authentication when we can

        String serverKeystore = MockHttpListenerWithAuthentication.class.getClassLoader()
                .getResource("cert/serverkeystore.jks").getPath();
        sslSocketConnector.setKeystore(serverKeystore);
        sslSocketConnector.setKeyPassword("serverpass");
        String serverTruststore = MockHttpListenerWithAuthentication.class.getClassLoader()
                .getResource("cert/servertruststore.jks").getPath();
        sslSocketConnector.setTruststore(serverTruststore);
        sslSocketConnector.setTrustPassword("serverpass");

        server.addConnector(socketConnector);
        server.addConnector(sslSocketConnector);

        SecurityHandler securityHandler = createBasicAuthenticationSecurityHandler();

        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(securityHandler);
        handlerList.addHandler(new AbstractHandler() {
            @Override
            public void handle(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
                System.out.println("uri: " + httpServletRequest.getRequestURI());
                System.out.println("queryString: " + httpServletRequest.getQueryString());
                System.out.println("method: " + httpServletRequest.getMethod());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(httpServletRequest.getInputStream(), baos);

                System.out.println("body: " + baos.toString());

                PrintWriter writer = httpServletResponse.getWriter();
                writer.append("testsvar");
                Random r = new Random();
                for (int j = 0; j < 10; j++) {
                    int value = r.nextInt(Integer.MAX_VALUE);
                    writer.append(value + "");
                }
                System.out.println();
                writer.close();
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            }
        });

        server.addHandler(handlerList);
        server.start();
    }

    private static SecurityHandler createBasicAuthenticationSecurityHandler() {
        Constraint constraint = new Constraint(Constraint.__BASIC_AUTH, "superuser");
        constraint.setAuthenticate(true);

        HashUserRealm myRealm = new HashUserRealm("MyRealm");
        myRealm.put("tobechanged", "tobechanged");
        myRealm.addUserToRole("tobechanged", "superuser");

        SecurityHandler securityHandler = new SecurityHandler();
        securityHandler.setUserRealm(myRealm);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");
        securityHandler.setConstraintMappings(new ConstraintMapping[]{constraintMapping});
        return securityHandler;
    }

}
