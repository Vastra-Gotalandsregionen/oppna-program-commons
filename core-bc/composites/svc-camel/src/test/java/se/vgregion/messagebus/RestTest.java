package se.vgregion.messagebus;

import org.apache.camel.*;
import org.apache.camel.component.cxf.CxfConstants;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Test;

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
                inMessage.setHeader(Exchange.ACCEPT_CONTENT_TYPE,"application/json");

                inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_RESPONSE_CLASS, String.class);
                inMessage.setBody(" ");
            }
        });

        Assert.assertNotNull(exchange.getOut());
    }
}
