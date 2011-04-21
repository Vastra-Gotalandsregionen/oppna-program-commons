package se.vgregion.messagebus.util;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class SimpleMessageListener implements javax.jms.MessageListener {

    public static boolean messageReceived = false;

    @Override
    public void onMessage(javax.jms.Message message) {
        try {
            System.out.println("Message received: " + ((TextMessage) message).getText());
            messageReceived = true;
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}