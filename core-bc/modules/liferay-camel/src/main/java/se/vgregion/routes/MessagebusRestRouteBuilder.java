package se.vgregion.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: pabe
 * Date: 2011-05-10
 * Time: 15:56
 */
public class MessagebusRestRouteBuilder extends SpringRouteBuilder {
    private static Logger log = LoggerFactory.getLogger(MessagebusJmsRouteBuilder.class);

    private String messageBusDestination;
    private String restDestination;

    private Map<String, String> exchangeIds = new HashMap<String, String>();

    public MessagebusRestRouteBuilder(String messageBusDestination, String restDestination) {
        this.messageBusDestination = messageBusDestination;
        this.restDestination = restDestination;
    }

    @Override
    public void configure() throws Exception {
        from("liferay:" + messageBusDestination)
                .log("1 b: ${body} h: ${header.JMSCorrelationID}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
                        exchangeIds.put(exchange.getExchangeId(), exchange.getIn().getHeader("JMSCorrelationId").toString());
                    }
                })
                .inOut("cxfrs://" + restDestination + "?synchronous=true")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getOut();

                        Response response = (Response) exchange.getIn().getBody();
                        InputStream inputStream = (InputStream) (response).getEntity();
                        BufferedInputStream bis = new BufferedInputStream(inputStream);
                        byte[] bytes = new byte[1024];
                        int n = bis.read(bytes);
                        String result = new String(bytes, 0, n, "UTF-8");
                        System.out.println("apa " + result);

                        exchange.getOut().setHeader("JMSCorrelationId", exchangeIds.get(exchange.getExchangeId()));
                        exchange.getOut().setBody(result);
                    }
                })
                .to("liferay:" + "liferay/message_bus/default_response" );
    }
}
