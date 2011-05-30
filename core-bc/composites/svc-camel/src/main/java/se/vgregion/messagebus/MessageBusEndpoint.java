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
 * @author Bruno Farache
 */
public class MessageBusEndpoint extends DefaultEndpoint {

	public MessageBusEndpoint(
		String uri, String remaining, Map params, Component component,
		MessageBus messageBus) {
		
		super(uri, component);

		_destination = remaining;
		_messageBus = messageBus;
	}

	public Consumer createConsumer(Processor processor) {
		return new MessageBusConsumer(this, processor);
	}

	public Producer createProducer() throws Exception {
		return new MessageBusProducer(this);
	}
	
	public boolean isSingleton() {
		return false;
	}
	
	public String getDestination() {
		return _destination;
	}
	
	public MessageBus getMessageBus() {
		return _messageBus;
	}
	
	private String _destination;
	private MessageBus _messageBus;

}