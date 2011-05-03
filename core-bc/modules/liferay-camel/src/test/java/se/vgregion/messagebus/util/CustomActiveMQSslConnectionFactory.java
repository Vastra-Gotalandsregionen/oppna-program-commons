package se.vgregion.messagebus.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.jms.JMSException;
import javax.net.ssl.*;

import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.broker.SslContext;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.tcp.SslTransportFactory;
import org.apache.activemq.util.JMSExceptionSupport;

public class CustomActiveMQSslConnectionFactory extends
        ActiveMQSslConnectionFactory {
    protected String trustStore;
    protected String trustStorePassword;
    protected String keyStore;
    protected String keyStorePassword;

    protected Transport createTransport() throws JMSException {
        // If the given URI is non-ssl, let superclass handle it.
        if (!brokerURL.getScheme().equals("ssl")) {
            return super.createTransport();
        }

        try {
            if (keyManager == null || trustManager == null) {
                trustManager = getTrustManager();
                keyManager = getKeyManager();
                // secureRandom can be left as null
            }
            SslTransportFactory sslFactory = new SslTransportFactory();
            SslContext ctx = new SslContext(keyManager, trustManager, secureRandom);
            SslContext.setCurrentSslContext(ctx);
            return sslFactory.doConnect(brokerURL);
        } catch (Exception e) {
            throw JMSExceptionSupport.create("Could not create Transport. Reason: " + e, e);
        }
    }


    public TrustManager[] getTrustManager() throws Exception {
        TrustManager[] trustStoreManagers = null;
        KeyStore trustedCertStore = KeyStore.getInstance("jks");

        InputStream tsStream = getClass().getResourceAsStream(trustStore);

        trustedCertStore.load(tsStream, trustStorePassword.toCharArray());
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        tmf.init(trustedCertStore);
        trustStoreManagers = tmf.getTrustManagers();
        return trustStoreManagers;
    }

    public KeyManager[] getKeyManager() throws Exception {
        KeyManagerFactory kmf =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance("jks");
        KeyManager[] keystoreManagers = null;

        byte[] sslCert = loadClientCredential(keyStore);


        if (sslCert != null && sslCert.length > 0) {
            ByteArrayInputStream bin = new ByteArrayInputStream(sslCert);
            ks.load(bin, keyStorePassword.toCharArray());
            kmf.init(ks, keyStorePassword.toCharArray());
            keystoreManagers = kmf.getKeyManagers();
        }
        return keystoreManagers;
    }

    private byte[] loadClientCredential(String fileName) throws IOException {
        if (fileName == null) {
            return null;
        }
        InputStream in = getClass().getResourceAsStream(fileName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int i = in.read(buf);
        while (i > 0) {
            out.write(buf, 0, i);
            i = in.read(buf);
        }
        in.close();
        return out.toByteArray();
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