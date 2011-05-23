package se.vgregion.routes;

import com.liferay.portal.kernel.messaging.DestinationNames;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: pabe
 * Date: 2011-05-10
 * Time: 15:56
 */
public class MessagebusRestRouteBuilder extends SpringRouteBuilder {

    private static Logger logger = LoggerFactory.getLogger(MessagebusRestRouteBuilder.class);

    private String messageBusDestination;
    private String restDestination;

    public MessagebusRestRouteBuilder(String messageBusDestination, String restDestination) {
        this.messageBusDestination = messageBusDestination;
        this.restDestination = restDestination;

        log.info("MB: {} ReST: {}", messageBusDestination, restDestination);
    }

    @Override
    public void configure() throws Exception {
        from("liferay:" + messageBusDestination)
                .errorHandler(deadLetterChannel("direct:error_" + messageBusDestination))
                .setHeader(Exchange.HTTP_METHOD, simple("POST"))
                .setProperty("correlationId", header("responseId"))
                .inOut("cxfrs://" + restDestination + "?synchronous=true")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String result = extractResponseBody(exchange);
                        exchange.getOut().setBody(result);
                    }
                })
                .setHeader("responseId", property("correlationId"))
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);

        from("direct:error_"+messageBusDestination)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        //Handle REST connection error
                        Exception routeException = new Exception();
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
                    }
                })
                .setHeader("responseId", property("correlationId"))
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);
    }

    private String extractResponseBody(Exchange exchange) throws IOException {
        Response response = (Response) exchange.getIn().getBody();
        InputStream inputStream = (InputStream) (response).getEntity();
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int n;
        StringBuilder sb = new StringBuilder();
        while ((n = bis.read(buffer)) > 0) {
            sb.append(new String(buffer, 0, n, "UTF-8"));
        }
        return sb.toString();
    }
}
