/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package se.vgregion.messagebus;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import se.vgregion.http.HttpRequest;

import java.util.Map;

/**
 * Endpoint message listener.
 * <p/>
 *
 * @author Bruno Farache
 */
public class EndpointMessageListener implements MessageListener {

    /**
     * Constructor.
     *
     * @param endpoint  endpoint.
     * @param processor processor.
     */
    public EndpointMessageListener(MessageBusEndpoint endpoint, Processor processor) {
        this.endpoint = endpoint;
        this.processor = processor;
    }

    /**
     * Outbound message.
     *
     * @param message message to send.
     */
    public void receive(Message message) {
        try {
            processor.process(createExchange(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Exchange createExchange(Message message) {
        Exchange exchange = new DefaultExchange(endpoint);
        org.apache.camel.Message in = new DefaultMessage();

        Object payload = message.getPayload();
        if (payload instanceof Map) {
            String query = HttpRequest.mapToQuery((Map) payload).toString();
            in.setBody(query);
        } else if (payload instanceof HttpRequest){
            String queryString = ((HttpRequest) payload).getQueryString();
            if (queryString != null) {
                in.setHeader(Exchange.HTTP_QUERY, queryString);
            }
            String body = ((HttpRequest) payload).getBody();
            if (body != null) {
                in.setBody(body);
            }
        } else {
            in.setBody(payload);
        }
        in.setHeader("responseId", message.getResponseId());

        Map<String, Object> params = endpoint.getParams();
        if (params != null) {
            String inHeaderKeys = (String) params.get("MessageInHeaders");
            if (inHeaderKeys != null) {
                for (String key : inHeaderKeys.split(",")) {
                    Object value = message.get(key);
                    if (value != null) {
                        in.setHeader(key, value);
                    }
                }
            }
        }

        exchange.setIn(in);

        return exchange;
    }

    private MessageBusEndpoint endpoint;
    private Processor processor;

}