package se.vgregion.routes;

import com.liferay.portal.kernel.messaging.DestinationNames;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.spring.SpringRouteBuilder;
import se.vgregion.http.AuthParams;
import se.vgregion.http.ProxyParams;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class for building camel routes with HTTP.
 * <p/>
 *
 * @author Patrik Bergström
 */
public class MessagebusHttpRouteBuilder extends SpringRouteBuilder {

    private String messageBusDestination;
    private String httpDestination;
    private AuthParams authParams;
    private ProxyParams proxyParams;
    private String httpClientConfigurerName;

    /**
     * Constructor.
     *
     * @param messageBusDestination messageBusDestination
     * @param httpDestination       restDestination
     * @param authParams            authParams
     */
    public MessagebusHttpRouteBuilder(String messageBusDestination, String httpDestination,
                                      AuthParams authParams, ProxyParams proxyParams) {
        this.messageBusDestination = messageBusDestination;
        this.httpDestination = httpDestination;
        this.authParams = authParams;
        this.proxyParams = proxyParams;

        log.info("MB: {} HTTP: {}", messageBusDestination, httpDestination);
    }

    /**
     * Constructor.
     *
     * @param messageBusDestination messageBusDestination
     * @param httpDestination       restDestination
     */
    public MessagebusHttpRouteBuilder(String messageBusDestination, String httpDestination) {
        this(messageBusDestination, httpDestination, null, null);
    }

    @Override
    public void configure() throws Exception {
        setHttpClientConfigurerName();

        String extraOptionsString = buildExtraOptionString();

        from("liferay:" + messageBusDestination)
                .errorHandler(deadLetterChannel("direct:error_" + messageBusDestination))
                .setProperty("correlationId", header("responseId"))
                        //if the body of the message is not null Camel will do a POST request
                .inOut(httpDestination + extraOptionsString)
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

    private void setHttpClientConfigurerName() {
        String[] httpClientConfigurers = getApplicationContext().getBeanNamesForType(HttpClientConfigurer.class);
        if (httpClientConfigurers.length == 1) {
            httpClientConfigurerName = httpClientConfigurers[0];
        } else if (httpClientConfigurers.length > 1) {
            throw new IllegalArgumentException("There shouldn't be more than one bean of type "
                    + "HttpClientConfigurer in the application context.");
        } else {
            httpClientConfigurerName = null;
        }
    }

    protected String buildExtraOptionString() {
        String extraOptionsString = "";
        if (httpClientConfigurerName != null) {
            extraOptionsString += "&httpClientConfigurer=" + httpClientConfigurerName;
        }
        if (authParams != null) {
            extraOptionsString += "&authUsername=" + authParams.getUsername() + "&authPassword="
                    + authParams.getPassword() + "&authMethod=" + authParams.getMethod();
        }
        if (proxyParams != null) {
            extraOptionsString += "&proxyHost=" + proxyParams.getHost() + "&proxyPort=" + proxyParams.getPort();
        }
        if (extraOptionsString.length() > 0) {
            //the first character is '&', replace it with '?'
            extraOptionsString = "?" + extraOptionsString.substring(1);
        }
        return extraOptionsString;
    }

    protected void errorHandler() {
        from("direct:error_" + messageBusDestination)
                .process(new ErrorProcessor())
                .setHeader("responseId", property("correlationId"))
                .to("liferay:" + DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE);
    }

    private String extractResponseBody(Exchange exchange) throws IOException {

        InputStream inputStream = null;
        BufferedInputStream bis = null;
        try {
            inputStream = (InputStream) exchange.getIn().getBody();
            bis = new BufferedInputStream(inputStream);
            final int i = 1024;
            byte[] buffer = new byte[i];
            int n;
            StringBuilder sb = new StringBuilder();
            while ((n = bis.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, n, "UTF-8"));
            }
            return sb.toString();
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
