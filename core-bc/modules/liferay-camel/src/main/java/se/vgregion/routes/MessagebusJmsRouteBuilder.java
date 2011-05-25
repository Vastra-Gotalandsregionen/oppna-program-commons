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

    private String messageBusDestination;
    private String activeMqDestination;

    /**
     * Constructor declaring messagebus destination and ActiveMq queue.
     *
     * @param messageBusDestination
     * @param activeMqDestination
     * @param brokerUrl
     */
    public MessagebusJmsRouteBuilder(String messageBusDestination, String activeMqDestination, String brokerUrl) {
        this.messageBusDestination = messageBusDestination;
        this.activeMqDestination = activeMqDestination;

        log.info("BrokerUrl: {}", brokerUrl);
        log.info("MessageBus: {} MQ: {}", messageBusDestination, activeMqDestination);
    }

    /**
     * Convenience constructor for tests
     *
     * @param messageBusDestination
     * @param activeMqDestination
     */
    public MessagebusJmsRouteBuilder(String messageBusDestination, String activeMqDestination) {
        this(messageBusDestination, activeMqDestination, null);
    }

    /**
     * Camel route definition.
     *
     * @throws Exception can fail.
     */
    @Override
    public void configure() throws Exception {
        from("liferay:" + messageBusDestination)
                .errorHandler(deadLetterChannel("direct:error_" + messageBusDestination))
                .setHeader("JMSCorrelationID", header("responseId"))
                .to("activemq:queue:" + activeMqDestination + "?preserveMessageQos=true&replyTo=" +
                        activeMqDestination + ".REPLY");

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
