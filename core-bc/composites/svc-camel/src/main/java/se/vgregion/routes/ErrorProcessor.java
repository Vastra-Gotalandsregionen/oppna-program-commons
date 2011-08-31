package se.vgregion.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Common class, implementing {@link Processor}, used by the {@link org.apache.camel.builder.RouteBuilder}s
 * in this package.
 * <p/>
 * User: pabe
 * Date: 2011-08-30
 * Time: 11:19
 */
public class ErrorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
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
}
