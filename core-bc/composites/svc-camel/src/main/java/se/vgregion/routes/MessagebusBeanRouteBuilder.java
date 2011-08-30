package se.vgregion.routes;

import com.liferay.portal.kernel.messaging.DestinationNames;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;

import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of Camel {@link org.apache.camel.builder.RouteBuilder} inheriting from
 * {@link SpringRouteBuilder}, which targets an arbitrary class and method.
 * <p/>
 * User: david
 * Date: 4/8-11
 * Time: 11:27
 */
public class MessagebusBeanRouteBuilder extends SpringRouteBuilder {
    private String messageBusDestination;
    private Class beanType;
    private String methodCall;

    /**
     * Constructor.
     *
     * @param messageBusDestination messageBusDestination
     * @param beanType              beanType
     * @param methodCall            methodCall
     */
    public MessagebusBeanRouteBuilder(String messageBusDestination, Class beanType, String methodCall) {
        this.messageBusDestination = messageBusDestination;
        this.beanType = beanType;
        this.methodCall = methodCall;
    }

    @Override
    public void configure() throws Exception {
        from("liferay:" + messageBusDestination)
                .errorHandler(deadLetterChannel("direct:error_" + messageBusDestination))
                .setProperty("correlationId", header("responseId"))
                .bean(beanType, methodCall)
                .setHeader("responseId", property("correlationId"))
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);

        from("direct:error_" + messageBusDestination)
                .process(new ErrorProcessor())
                .setHeader("responseId", property("correlationId"))
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);
    }
}
