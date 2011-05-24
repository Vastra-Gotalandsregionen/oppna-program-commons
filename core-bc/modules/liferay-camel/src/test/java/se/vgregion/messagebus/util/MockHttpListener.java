package se.vgregion.messagebus.util;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * User: pabe
 * Date: 2011-05-23
 * Time: 16:59
 */
public class MockHttpListener {

    private static Server server = new Server(8899);

    public static void main(String[] args) throws Exception {
        server.addHandler(new AbstractHandler() {
            @Override
            public void handle(String s, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
                System.out.println(httpServletRequest);
                /*try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }*/
                PrintWriter writer = httpServletResponse.getWriter();
                writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                        "<createUserResponse xmlns=\"http://portal.vgregion.se/createuser\">" +
                        "<vgrId>ex_test</vgrId>" +
                        "<statusCode>NEW_USER</statusCode>" +
                        "<message>message</message>" +
                        "</createUserResponse>");
                writer.close();
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            }
        });
        server.start();
    }
}
