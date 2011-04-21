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

        //Send to SimpleMessageListener
        jmsTemplate.send("testQueue", new MessageCreator() {
            @Override
            public javax.jms.Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("A test message");
            }
        });

        Thread.sleep(100);

        assertEquals(true, SimpleMessageListener.messageReceived);
    }

    @DirtiesContext
    public void testMessageBusToJms() throws Exception{
        SimpleMessageListener.messageReceived = false;
        final List<Object> list = sendToMessageBus();

        Thread.sleep(100);

        assertEquals(true, SimpleMessageListener.messageReceived);
    }

    @DirtiesContext
    public void testMessageBus() throws Exception {
        final List<Object> list = sendToMessageBus();

        Thread.sleep(100);

        assertEquals(1, list.size());
    }

    private List<Object> sendToMessageBus() {
        final List<Object> list = new ArrayList();

        final String payload = "test payload";
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