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
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.SerialDestination;

import java.io.File;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.converter.IOConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * @author Bruno Farache
 */
@ContextConfiguration(
	locations={
		"/META-INF/message-bus-spring-test.xml", "/META-INF/camel-spring-test.xml",
		"/META-INF/test-routes.xml", "/META-INF/jms-messaging.xml"})
public class MessageBusComponentTest extends AbstractJUnit38SpringContextTests {
	
	@DirtiesContext
    public void testProducer() throws Exception {
    	messageBus.addDestination(new SerialDestination("destination"));

		messageBus.registerMessageListener(
			"destination",
			new MessageListener() {
	
				public void receive(Message message) {
					result = message.getPayload().toString();
				}
			
			});
		
		String testString = "testing";
        Message message = new Message();
        message.setPayload(testString);

		template.sendBody("direct:testProducer", message);
		
		Thread.sleep(500);

		assertEquals(testString, result);
    }
    
    @DirtiesContext
    public void testConsumer() throws Exception {
    	Message message = new Message();
    	message.setPayload("testing");
    	
    	messageBus.sendMessage("testConsumer", message);
    	
    	resultEndpoint.expectedMessageCount(1);
    	resultEndpoint.expectedBodiesReceived("testing");
    
    	resultEndpoint.assertIsSatisfied();
    }
    
    @DirtiesContext
    public void testFile() throws Exception {
        File file = null;
        try {
            String testString = "testing";

            Message message = new Message();
            message.setPayload(testString);

            messageBus.sendMessage("testFile", message);

            Thread.sleep(1000);

            file = new File("bin/testFile.txt");

            assertEquals(testString, IOConverter.toString(file, null));
        } finally {
            file.delete();
        }

    }
    
    public static String result;
    
    @Autowired
    private MessageBus messageBus;

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:testProducer")
    protected ProducerTemplate template;
	
}