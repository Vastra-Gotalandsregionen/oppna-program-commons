package se.vgregion.messagebus.util;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

public class SimpleMessageListener implements javax.jms.MessageListener {

    private JmsTemplate jmsTemplate;

    public void setJmsTemplate(JmsTemplate template) {
        this.jmsTemplate = template;
    }

    public static boolean messageReceived = false;

    public static String readMessage = "";

    @Override
    public void onMessage(final javax.jms.Message message) {
        try {
            readMessage = ((TextMessage) message).getText();
            System.out.println("Message received: " + readMessage);
            messageReceived = true;

            Destination dest = message.getJMSReplyTo();
            if (dest != null) {
                jmsTemplate.send(dest, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage textMessage = session.createTextMessage(readMessage.toUpperCase());
                        textMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                        return textMessage;
                    }
                });
            }


        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}