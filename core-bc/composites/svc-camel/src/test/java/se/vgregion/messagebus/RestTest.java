package se.vgregion.messagebus;

import org.apache.camel.*;
import org.apache.camel.component.cxf.CxfConstants;
import org.apache.camel.component.restlet.RestletConstants;
import org.apache.camel.component.restlet.RestletEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 3/8-11
 * Time: 12:59
 */
public class RestTest extends CamelTestSupport {


    @Test
    public void callTestCounter() {

        Exchange exchange = template.send("cxfrs://http://www.google.com",
                new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.setPattern(ExchangePattern.InOut);
                        Message inMessage = exchange.getIn();
                        inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.TRUE);
                        inMessage.setHeader(Exchange.HTTP_METHOD, "GET");
//                inMessage.setHeader(Exchange.HTTP_METHOD, "POST");
//                inMessage.setHeader(Exchange.HTTP_PATH,"/");
                        inMessage.setHeader(Exchange.ACCEPT_CONTENT_TYPE, "application/json");

                        inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_RESPONSE_CLASS, String.class);
                        inMessage.setBody(" ");
                    }
                });

        Assert.assertNotNull(exchange.getOut());
    }

    @Ignore
    @Test
    public void callNotesCalendar() {
        RestletEndpoint ep = context.getEndpoint("restlet://http://aida.vgregion.se", RestletEndpoint.class);
        ep.setUriPattern("/calendar.nsf/getinfo?openagent&userid=susro3&year=2011&month=6&day=1&period=3");
        System.out.println("EP: " + ep.getEndpointUri());

        Exchange exchange = template.send(ep, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.setPattern(ExchangePattern.InOut);
                Message inMessage = exchange.getIn();
//                inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.TRUE);
                inMessage.setHeader(Exchange.HTTP_METHOD, "GET");
                inMessage.setHeader(Exchange.ACCEPT_CONTENT_TYPE, "*/*");

//                inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_RESPONSE_CLASS, String.class);
                inMessage.setBody(" ");
            }
        });

        Assert.assertNotNull(exchange.getOut());
        System.out.println(exchange.getOut());
    }

    @Ignore
    @Test
    public void restletCallNotes() {
        List results = new ArrayList();
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            Exchange exchange1 = template.request("restlet://http://aida.vgregion.se/calendar.nsf/unreadcount?openagent&userid=susro3",
                    new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            exchange.setPattern(ExchangePattern.InOut);
                            Message inMessage = exchange.getIn();
                            inMessage.setHeader(Exchange.HTTP_METHOD, "GET");
                            inMessage.setHeader(Exchange.ACCEPT_CONTENT_TYPE, "*/*");

                inMessage.setHeader(RestletConstants.RESTLET_LOGIN, "susro3");
                inMessage.setHeader(RestletConstants.RESTLET_PASSWORD, "");
                inMessage.setBody(" ");
            }
        });



            Exchange exchange = template.request("restlet://http://localhost:8080/mobile-icon-core-bc-module-intsvc/test-counter", new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    exchange.setPattern(ExchangePattern.InOut);
                    Message inMessage = exchange.getIn();
                    inMessage.setHeader(Exchange.HTTP_METHOD, "GET");
                    inMessage.setHeader(Exchange.ACCEPT_CONTENT_TYPE, "*/*");
                }
            });

            Assert.assertNotNull(exchange.getOut());
            System.out.println(i + ": " + exchange.getOut().toString() + " " + (System.currentTimeMillis()
                    - start));
        }
        System.out.println(results);

    }
}
