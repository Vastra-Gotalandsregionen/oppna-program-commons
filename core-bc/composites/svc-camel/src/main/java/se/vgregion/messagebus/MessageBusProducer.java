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
import com.liferay.portal.kernel.messaging.MessageBus;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * A {@link org.apache.camel.Producer} implementation for use with Liferay's
 * {@link com.liferay.portal.kernel.messaging.MessageBus}.
 *
 * @author Bruno Farache
 */
public class MessageBusProducer extends DefaultProducer {

    /**
     * Constructor.
     *
     * @param endpoint endpoint
     */
    public MessageBusProducer(MessageBusEndpoint endpoint) {
        super(endpoint);
    }

    /**
     * Process the <code>Exchange</code>.
     *
     * @param exchange exchange
     * @throws Exception Exception
     */
    public void process(Exchange exchange) throws Exception {
        se.vgregion.messagebus.MessageBusEndpoint endpoint = (MessageBusEndpoint) getEndpoint();
        MessageBus messageBus = endpoint.getMessageBus();

        String destination = endpoint.getDestination();

        Message body = exchange.getIn().getBody(Message.class);
        body.setResponseId((String) exchange.getIn().getHeader("responseId"));

        messageBus.sendMessage(destination, body);
    }

}