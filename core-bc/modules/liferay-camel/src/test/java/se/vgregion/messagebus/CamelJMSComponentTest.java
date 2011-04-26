package se.vgregion.messagebus;

import se.vgregion.messagebus.util.SimpleMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

import javax.jms.JMSException;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Farache
 */
@ContextConfiguration(
        locations = {
                "/META-INF/message-bus-spring-test.xml", "/META-INF/camel-spring-test.xml",
                "/META-INF/test-routes.xml", "/META-INF/jms-messaging.xml"})
public class CamelJMSComponentTest extends AbstractJUnit38SpringContextTests {

    @DirtiesContext
    public void testJMSMessage() throws Exception {
        SimpleMessageListener.messageReceived = false;
        final String msg = "A test message";

        //Send to SimpleMessageListener
        jmsTemplate.send("TEST.QUEUE", new MessageCreator() {
            @Override
            public javax.jms.Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });

        Thread.sleep(100);

        assertEquals(true, SimpleMessageListener.messageReceived);
        assertEquals(msg, SimpleMessageListener.readMessage);
    }

    @DirtiesContext
    public void testMessageBusToJms() throws Exception{
        SimpleMessageListener.messageReceived = false;
        String payload = "test payload";

        final List<Object> list = sendToMessageBus(payload);

        Thread.sleep(100);

        assertEquals(true, SimpleMessageListener.messageReceived);
        assertEquals(payload, SimpleMessageListener.readMessage);
    }

    @DirtiesContext
    public void testMessageBus() throws Exception {
        String payload = "test payload";
        final List<Object> list = sendToMessageBus(payload);

        Thread.sleep(100);

        assertEquals(1, list.size());
    }

    @DirtiesContext
    public void testCamelQueueReply() throws Exception {
        final String payload = "apa bepa";
        Message message = new Message();
        message.setPayload(payload);

        final List<Object> list = new ArrayList();

        messageBus.registerMessageListener("vgr/testCamelDestinationReply", new MessageListener() {
            @Override
            public void receive(Message message) {
                assertEquals(payload.toUpperCase(), message.getPayload());
                list.add(new Object());
            }
        });

        messageBus.sendMessage("vgr/testCamelDestination", message);

        Thread.sleep(1000);

        assertEquals(1, list.size());
    }


    private List<Object> sendToMessageBus(final String payload) {
        final List<Object> list = new ArrayList();

        String destinationName = "vgr/testJmsDestination";

        Message message = new Message();
        message.setPayload(payload);

        messageBus.registerMessageListener(destinationName, new MessageListener() {
            @Override
            public void receive(Message message) {
                assertEquals(payload, message.getPayload());
                list.add(new Object());
            }
        });

        messageBus.sendMessage(destinationName, message);
        return list;
    }

    @Autowired
    private MessageBus messageBus;
    @Autowired
    private JmsTemplate jmsTemplate;

}