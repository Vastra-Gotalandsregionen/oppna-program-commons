package se.vgregion.messagebus.util;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * User: pabe
 * Date: 2011-04-29
 * Time: 11:05
 */
public class ActiveMQListener {

    public static void main(String[] args) throws JMSException {
        final String replyText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><activateUserResponse xmlns=\"" +
                                "http://portal.vgregion.se/activateuser\">" +
                                "<userId>theuserid</userId><statusCode>SUCCESS</statusCode>" +
                                "<message>The message</message></activateUserResponse>";

//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("ssl://localhost:61617");
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        final Session session = connection.createSession(false, 1);
        connection.start();
        Destination destinationAdvisory = session.createQueue("PORTAL.SE.ACTIVATEUSER.QUEUE");
        MessageConsumer consumerAdvisory = session.createConsumer(destinationAdvisory);
        consumerAdvisory.setMessageListener(new javax.jms.MessageListener() {
            @Override
            public void onMessage(javax.jms.Message message) {
                try {
                    System.out.println("Received message: " + message);
                    TextMessage textMessage = session.createTextMessage(replyText);
                    textMessage.setJMSCorrelationID(message.getJMSCorrelationID());

                    Destination jmsReplyTo = message.getJMSReplyTo();
                    MessageProducer messageProducer = session.createProducer(jmsReplyTo);
                    messageProducer.send(jmsReplyTo, textMessage);
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }
        });

        while (true)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
