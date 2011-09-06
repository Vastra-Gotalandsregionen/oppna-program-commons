package se.vgregion.routes;

import com.liferay.portal.kernel.messaging.DestinationNames;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;

import java.net.InetAddress;

/**
 * Class for building camel routes with JMS.
 * <p/>
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
     * @param messageBusDestination messageBusDestination
     * @param activeMqDestination   activeMqDestination
     * @param brokerUrl             brokerUrl
     */
    public MessagebusJmsRouteBuilder(String messageBusDestination, String activeMqDestination, String brokerUrl) {
        this.messageBusDestination = messageBusDestination;
        this.activeMqDestination = activeMqDestination;

        log.info("BrokerUrl: {}", brokerUrl);
        log.info("MessageBus: {} MQ: {}", messageBusDestination, activeMqDestination);
    }

    /**
     * Convenience constructor for tests.
     *
     * @param messageBusDestination messageBusDestination
     * @param activeMqDestination   activeMqDestination
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
        String replyTo = activeMqDestination + ".REPLY." + InetAddress.getLocalHost().getHostName();
        from("liferay:" + messageBusDestination)
                .errorHandler(deadLetterChannel("direct:error_" + messageBusDestination))
                .setHeader("JMSCorrelationID", header("responseId"))
                .to("activemq:queue:" + activeMqDestination + "?preserveMessageQos=true&replyTo="
                        + replyTo);

        from("activemq:queue:" + replyTo + "?disableReplyTo=true")
                .setHeader("responseId", header("JMSCorrelationID"))
                .to("log:se.vgregion.routes?level=INFO")
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);

        from("direct:error_" + messageBusDestination)
                .process(new CustomErrorProcessor())
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);
    }

    //It is more efficient to have this as a static inner class instead of an anonymous class according to
    //FindBugs.
    private static class CustomErrorProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            //First delegate to the common ErrorProcessor
            new ErrorProcessor().process(exchange);
            //Then add customized behaviour
            String responseId = (String) exchange.getIn().getHeader("JMSCorrelationID");
            exchange.getOut().setHeader("responseId", responseId);
        }
    }
}
