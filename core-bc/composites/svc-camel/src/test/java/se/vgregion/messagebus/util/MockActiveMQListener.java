package se.vgregion.messagebus.util;

import se.vgregion.ssl.jms.ActiveMqSslConnectionFactory;

import javax.jms.*;

/**
 * User: pabe
 * Date: 2011-04-29
 * Time: 11:05
 */
public class MockActiveMQListener {

    public static void main(String[] args) throws JMSException {
        final String activateUserReplyText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><activateUserResponse xmlns=\"" +
                                "http://portal.vgregion.se/activateuser\">" +
                                "<userId>theuserid</userId><statusCode>SUCCESS</statusCode>" +
                                "<message>The message</message></activateUserResponse>";
        final String inviteUserReplyText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><inviteUserResponse xmlns=\"http://portal.vgregion.se/inviteuser\"><userId>ex_apa</userId><statusCode>SUCCESS</statusCode><message>the reply message</message></inviteUserResponse>";

        ActiveMqSslConnectionFactory connectionFactory = new ActiveMqSslConnectionFactory();
        connectionFactory.setBrokerURL("ssl://localhost:61617");
        connectionFactory.setTrustStore("/client.ts");
        connectionFactory.setTrustStorePassword("password");

//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        final Session session = connection.createSession(false, 1);
        connection.start();

        String activateUserQueue = "PORTAL.SE.ACTIVATEUSER.QUEUE";
        String inviteUserQueue = "PORTAL.SE.INVITEUSER.QUEUE";

        startQueueListener(activateUserReplyText, session, activateUserQueue);
        startQueueListener(inviteUserReplyText, session, inviteUserQueue);

        while (true)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    private static void startQueueListener(final String replyText, final Session session, String activateUserQueue) throws JMSException {
        Destination destinationAdvisory = session.createQueue(activateUserQueue);
        MessageConsumer consumerAdvisory = session.createConsumer(destinationAdvisory);
        consumerAdvisory.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
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
    }
}
