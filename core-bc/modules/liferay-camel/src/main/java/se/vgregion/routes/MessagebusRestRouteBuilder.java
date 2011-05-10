package se.vgregion.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: pabe
 * Date: 2011-05-10
 * Time: 15:56
 */
public class MessagebusRestRouteBuilder extends SpringRouteBuilder {
    private static Logger log = LoggerFactory.getLogger(MessagebusJmsRouteBuilder.class);

    private String messageBusDestination;
    private String restDestination;

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
                    }
                })
//                .to("cxfrs://" + restDestination + "?synchronous=true")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getOut().setBody("<simulerat>svar</simulerat>");
                    }
                })
                .to("liferay:" + messageBusDestination + ".REPLY");
    }
}
