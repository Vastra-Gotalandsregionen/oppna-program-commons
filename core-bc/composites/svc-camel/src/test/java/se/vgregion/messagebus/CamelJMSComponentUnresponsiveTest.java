package se.vgregion.messagebus;

import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageListener;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.messagebus.util.SimpleMessageListener;

import javax.jms.JMSException;
import javax.jms.Session;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Bruno Farache
 */
@ContextConfiguration(
        locations = {"/META-INF/camelJmsComponentUnresponsiveTest.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CamelJMSComponentUnresponsiveTest {

    private static final Logger log = Logger.getLogger(CamelJMSComponentUnresponsiveTest.class);

    @Value("${messagebus.destination}")
    String messagebusDestination;
    @Value("${activemq.destination}")
    String activemqDestination;

    @Test
    @DirtiesContext
    public void testCamelQueueReply() throws Exception {
        final String payload = "apa bepa";
        String correlationId = UUID.randomUUID().toString();

        Message message = new Message();
        message.setPayload(payload);
        message.setResponseId(correlationId);

        final List<Message> list = new ArrayList();

        messageBus.registerMessageListener(DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE, new MessageListener() {
            @Override
            public void receive(Message message) {
                assertTrue(message.getPayload() instanceof Exception);
                list.add(message);
            }
        });

        messageBus.sendMessage(messagebusDestination, message);

        Thread.sleep(500);

        assertEquals(1, list.size());
        assertEquals(((Message)list.get(0)).getResponseId(), correlationId);
    }

    @Autowired
    private MessageBus messageBus;
//    @Autowired
//    private JmsTemplate jmsTemplate;

}