package se.vgregion.routes;

import com.liferay.portal.kernel.messaging.DestinationNames;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: pabe
 * Date: 2011-04-27
 * Time: 10:29
 */
public class MessagebusJmsRouteBuilder extends SpringRouteBuilder {
    private static Logger log = LoggerFactory.getLogger(MessagebusJmsRouteBuilder.class);

    private String brokerUrl;
    private String messageBusDestination;
    private String activeMqDestination;

    public MessagebusJmsRouteBuilder(String messageBusDestination, String activeMqDestination, String brokerUrl) {
        this.messageBusDestination = messageBusDestination;
        this.activeMqDestination = activeMqDestination;
        this.brokerUrl = brokerUrl;

        log.info("BrokerUrl: {}", brokerUrl);
        log.info("MessageBus: {} MQ: {}", messageBusDestination, activeMqDestination);
    }

    public MessagebusJmsRouteBuilder(String messageBusDestination, String activeMqDestination) {
        this(messageBusDestination, activeMqDestination, null);
    }

    @Override
    public void configure() throws Exception {
        from("liferay:" + messageBusDestination)
                .errorHandler(deadLetterChannel("direct:error_" + messageBusDestination))
                .setHeader("JMSCorrelationID", header("responseId"))
                .to("activemq:queue:" + activeMqDestination + "?preserveMessageQos=true&replyTo=" + activeMqDestination + ".REPLY");

        from("activemq:queue:" + activeMqDestination + ".REPLY?disableReplyTo=true")
                .setHeader("responseId", header("JMSCorrelationID"))
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);

        from("direct:error_" + messageBusDestination)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        //Handle connection error
                        Object exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
                        if (exception instanceof Throwable) {
                            Throwable ex = (Throwable) exception;
                            while (ex.getCause() != null) {
                                ex = ex.getCause();
                            }
                            if (ex.getClass().getPackage().getName().startsWith("java.net")) {
                                exchange.getOut().setBody(ex);
                            } else {
                                exchange.getOut().setBody(new Exception(((Throwable) exception).getMessage()));
                            }
                        } else {
                            exchange.getOut().setBody(new Exception("Unknown error"));
                        }
                        String responseId = (String) exchange.getIn().getHeader("JMSCorrelationID");
                        exchange.getOut().setHeader("responseId", responseId);
                    }
                })
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);
    }
}
