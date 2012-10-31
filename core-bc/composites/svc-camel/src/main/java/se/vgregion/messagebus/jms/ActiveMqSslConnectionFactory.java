package se.vgregion.messagebus.jms;

import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.transport.Transport;
import se.vgregion.ssl.ConvenientSslContextFactory;

import javax.jms.JMSException;

public final class ActiveMqSslConnectionFactory extends ActiveMQSslConnectionFactory {
    private String trustStore;
    private String trustStorePassword;
    private String keyStore;
    private String keyStorePassword;

    protected Transport createTransport() throws JMSException {
        // If the given URI is non-ssl, let superclass handle it.
        if (!brokerURL.getScheme().equals("ssl")) {
            return super.createTransport();
        }

        ConvenientSslContextFactory convenientSslContextFactory = new ConvenientSslContextFactory(trustStore,
                trustStorePassword, keyStore, keyStorePassword);

        if (keyManager == null && notEmpty(keyStore)) {
            keyManager = convenientSslContextFactory.getKeyManagers();
        }

        if (trustManager == null && notEmpty(trustStore)) {
            trustManager = convenientSslContextFactory.getTrustManagers();
        }

        return super.createTransport();
    }

    private boolean notEmpty(String string) {
        return string != null && !string.equals("");
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

}