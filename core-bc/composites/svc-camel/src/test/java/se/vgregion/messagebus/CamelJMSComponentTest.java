package se.vgregion.messagebus;

import com.liferay.portal.kernel.dao.orm.*;
import com.liferay.portal.kernel.executor.PortalExecutorManager;
import com.liferay.portal.kernel.executor.PortalExecutorManagerUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageListener;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
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

import javax.jms.*;
import javax.jms.Session;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"/META-INF/camelJmsComponentTest.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CamelJMSComponentTest {

    private static final Logger log = Logger.getLogger(CamelJMSComponentTest.class);

    @Value("${messagebus.destination}")
    String messagebusDestination;
    @Value("${activemq.destination}")
    String activemqDestination;

    @Test
    @DirtiesContext
    public void testJMSMessage() throws Exception {
        SimpleMessageListener.messageReceived = false;
        final String msg = "A test message";

        //Send to SimpleMessageListener
        jmsTemplate.send(activemqDestination, new MessageCreator() {
            @Override
            public javax.jms.Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });

        Thread.sleep(100);

        assertEquals(true, SimpleMessageListener.messageReceived);
        assertEquals(msg, SimpleMessageListener.readMessage);
    }

    @Test
    @DirtiesContext
    public void testMessageBusToJms() throws Exception {
        SimpleMessageListener.messageReceived = false;
        String payload = "test payload";

        final List<Object> list = sendToMessageBus(payload);

        Thread.sleep(1000);

        assertEquals(true, SimpleMessageListener.messageReceived);
        assertEquals(payload, SimpleMessageListener.readMessage);
    }

    @Test
    @DirtiesContext
    public void testMessageBus() throws Exception {
        String payload = "test payload";
        final List<Object> list = sendToMessageBus(payload);

        Thread.sleep(100);

        assertEquals(1, list.size());
    }

    @Test
    @DirtiesContext
    public void testCamelQueueReply() throws Exception {
        final String payload = "apa bepa";
        List<String> correlationIds = new ArrayList<String>();
        correlationIds.add(UUID.randomUUID().toString());
        correlationIds.add(UUID.randomUUID().toString());


        Message message = new Message();
        message.setPayload(payload);
        message.setResponseId(correlationIds.get(0));
        Message message2 = new Message();
        message2.setPayload(payload);
        message2.setResponseId(correlationIds.get(1));

        final List<Message> list = new ArrayList();

        messageBus.registerMessageListener(DestinationNames.MESSAGE_BUS_DEFAULT_RESPONSE, new MessageListener() {
            @Override
            public void receive(Message message) {
                assertEquals(payload.toUpperCase(), message.getPayload());
                list.add(message);
            }
        });

        messageBus.sendMessage(messagebusDestination, message);
        Thread.sleep(100);
        messageBus.sendMessage(messagebusDestination, message2);

        Thread.sleep(500);

        assertEquals(2, list.size());
        for (Message m : list) {
            assertTrue(correlationIds.contains(m.getResponseId()));
            correlationIds.remove(m.getResponseId());
        }

        assertEquals(0, correlationIds.size());
    }


    private List<Object> sendToMessageBus(final String payload) {
        final List<Object> list = new ArrayList();

        String destinationName = messagebusDestination;

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


    @BeforeClass
    public static void init() {
        new PortalExecutorManagerUtil().setPortalExecutorManager(new PortalExecutorManager() {
            @Override
            public <T> Future<T> execute(String name, Callable<T> callable) {
                return null;
            }

            @Override
            public <T> T execute(String name, Callable<T> callable, long timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }

            @Override
            public com.liferay.portal.kernel.concurrent.ThreadPoolExecutor getPortalExecutor(String name) {
                return null;
            }

            @Override
            public com.liferay.portal.kernel.concurrent.ThreadPoolExecutor getPortalExecutor(String name, boolean createIfAbsent) {
                return null;
            }

            @Override
            public com.liferay.portal.kernel.concurrent.ThreadPoolExecutor registerPortalExecutor(String name, com.liferay.portal.kernel.concurrent.ThreadPoolExecutor threadPoolExecutor) {
                return null;
            }

            @Override
            public void shutdown() {

            }

            @Override
            public void shutdown(boolean interrupt) {

            }

            @Override
            public void shutdown(String name) {

            }

            @Override
            public void shutdown(String name, boolean interrupt) {

            }
        });

        new EntityCacheUtil().setEntityCache(new EntityCache() {
            @Override
            public void clearCache() {

            }

            @Override
            public void clearCache(String className) {

            }

            @Override
            public void clearLocalCache() {

            }

            @Override
            public Serializable getResult(boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey) {
                return null;
            }

            @Override
            public void invalidate() {

            }

            @Override
            public Serializable loadResult(boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey, SessionFactory sessionFactory) {
                return null;
            }

            @Override
            public void putResult(boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey, Serializable result) {

            }

            @Override
            public void removeCache(String className) {

            }

            @Override
            public void removeResult(boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey) {

            }
        });

        new FinderCacheUtil().setFinderCache(new FinderCache() {
            @Override
            public void clearCache() {

            }

            @Override
            public void clearCache(String className) {

            }

            @Override
            public void clearLocalCache() {

            }

            @Override
            public Object getResult(FinderPath finderPath, Object[] args, SessionFactory sessionFactory) {
                return null;
            }

            @Override
            public void invalidate() {

            }

            @Override
            public void putResult(FinderPath finderPath, Object[] args, Object result) {

            }

            @Override
            public void removeCache(String className) {

            }

            @Override
            public void removeResult(FinderPath finderPath, Object[] args) {

            }
        });
    }


}