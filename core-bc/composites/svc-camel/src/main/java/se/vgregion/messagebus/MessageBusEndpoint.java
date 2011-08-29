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

import com.liferay.portal.kernel.messaging.MessageBus;

import java.util.Map;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * An implementation of {@link org.apache.camel.Endpoint} based on {@link DefaultEndpoint} for use with Liferay's
 * {@link MessageBus}.
 * <p/>
 * @author Bruno Farache
 */
public class MessageBusEndpoint extends DefaultEndpoint {

    /**
     * Constructor.
     *
     * @param uri        uri
     * @param remaining  remaining
     * @param params     params
     * @param component  component
     * @param messageBus messageBus
     */
    public MessageBusEndpoint(
            String uri, String remaining, Map<String, Object> params, Component component,
            MessageBus messageBus) {

        super(uri, component);

        destination = remaining;
        this.messageBus = messageBus;
        this.params = params;

    }

    /**
     * Creates a <code>Consumer</code>.
     *
     * @param processor processor
     * @return The created <code>Consumer</code>.
     */
    public Consumer createConsumer(Processor processor) {
        return new MessageBusConsumer(this, processor);
    }

    /**
     * Creates a <code>Producer</code>.
     *
     * @return The created <code>Producer</code>.
     * @throws Exception Exception
     */
    public Producer createProducer() throws Exception {
        return new MessageBusProducer(this);
    }

    public boolean isSingleton() {
        return false;
    }

    public String getDestination() {
        return destination;
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public boolean isLenientProperties() {
        return true;
    }

    private String destination;
    private MessageBus messageBus;
    private Map<String, Object> params;


}