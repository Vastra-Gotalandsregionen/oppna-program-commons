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
 * Class for building camel routes with REST.
 * <p/>
 * User: pabe
 * Date: 2011-05-10
 * Time: 15:56
 */
public class MessagebusRestRouteBuilder extends SpringRouteBuilder {

    private String messageBusDestination;
    private String restDestination;
    private String restMethod;
    private String restContentType;

    /**
     * Constructor.
     *
     * @param messageBusDestination messageBusDestination
     * @param restDestination restDestination
     * @param restMethod restMethod
     * @param restContentType restContentType
     */
    public MessagebusRestRouteBuilder(String messageBusDestination, String restDestination, String restMethod,
                                      String restContentType) {
        this.messageBusDestination = messageBusDestination;
        this.restDestination = restDestination;
        this.restMethod = restMethod;
        this.restContentType = restContentType;

        log.info("MB: {} ReST: {}", messageBusDestination, restDestination);
    }

    /**
     * Constructor.
     *
     * @param messageBusDestination messageBusDestination
     * @param restDestination restDestination
     */
    public MessagebusRestRouteBuilder(String messageBusDestination, String restDestination) {
        this(messageBusDestination, restDestination, "POST", "*/*");
    }

    @Override
    public void configure() throws Exception {

        from("liferay:" + messageBusDestination)
                .errorHandler(deadLetterChannel("direct:error_" + messageBusDestination))
                .setHeader(Exchange.HTTP_METHOD, simple(restMethod))
                .setHeader(Exchange.ACCEPT_CONTENT_TYPE, simple(restContentType))
                .setProperty("correlationId", header("responseId"))
                .inOut("cxfrs://" + restDestination)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String result = extractResponseBody(exchange);
                        exchange.getOut().setBody(result);
                    }
                })
                .setHeader("responseId", property("correlationId"))
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);

        errorHandler();
    }

    protected void errorHandler() {
        from("direct:error_" + messageBusDestination)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        //Handle REST connection error
                        Object exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
                        if (exception instanceof Throwable) {
                            Throwable ex = (Throwable) exception;
                            while (ex.getCause() != null) {
                                ex = ex.getCause();
                            }
                            if (ex.getClass().getPackage().getName().startsWith("java.net")) {
                                exchange.getOut().setBody(ex);
                            } else {
                                exchange.getOut().setBody(new Exception(((Throwable) exception).getMessage(), ex));
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

        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            inputStream = (InputStream) (response).getEntity();
            bis = new BufferedInputStream(inputStream);
            final int i = 1024;
            byte[] buffer = new byte[i];
            int n;
            StringBuilder sb = new StringBuilder();
            while ((n = bis.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, n, "UTF-8"));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
