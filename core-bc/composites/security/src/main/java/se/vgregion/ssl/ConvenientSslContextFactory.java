package se.vgregion.ssl;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

/**
 * @author Patrik Bergström
 */
public class ConvenientSslContextFactory {

    private String trustStore;
    private String trustStorePassword;
    private String keyStore;
    private String keyStorePassword;

    public ConvenientSslContextFactory(String trustStore, String trustStorePassword, String keyStore,
                                       String keyStorePassword) {
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
        this.keyStore = keyStore;
        this.keyStorePassword = keyStorePassword;
    }

    public SSLContext createSslContext() throws Exception {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(getKeyManagers(), getTrustManagers(), null);
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get {@link javax.net.ssl.TrustManager} array.
     * @return Array of {@link javax.net.ssl.TrustManager}s.
     * @throws Exception Exception
     */
    public TrustManager[] getTrustManagers() {
        TrustManager[] trustStoreManagers;

        InputStream tsStream = null;
        try {
            KeyStore trustedCertStore = KeyStore.getInstance("jks");
            tsStream = getClass().getClassLoader().getResourceAsStream(trustStore);

            trustedCertStore.load(tsStream, trustStorePassword.toCharArray());

            TrustManagerFactory tmf =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            tmf.init(trustedCertStore);
            trustStoreManagers = tmf.getTrustManagers();
            return trustStoreManagers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (tsStream != null) {
                try {
                    tsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get {@link javax.net.ssl.KeyManager} array.
     * @return Array of {@link javax.net.ssl.KeyManager}s.
     * @throws Exception Exception
     */
    public KeyManager[] getKeyManagers() {
        ByteArrayInputStream bin = null;
        try {
            KeyManagerFactory kmf =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore ks = KeyStore.getInstance("jks");
            KeyManager[] keystoreManagers = null;

            byte[] sslCert = loadClientCredential(keyStore);

            if (sslCert != null && sslCert.length > 0) {
                bin = new ByteArrayInputStream(sslCert);
                ks.load(bin, keyStorePassword.toCharArray());
                kmf.init(ks, keyStorePassword.toCharArray());
                keystoreManagers = kmf.getKeyManagers();
            }
            return keystoreManagers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] loadClientCredential(String fileName) throws IOException {
        if (fileName == null) {
            return new byte[0];
        }

        InputStream in = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(fileName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            final int i1 = 512;
            byte[] buf = new byte[i1];
            int i = in.read(buf);
            while (i > 0) {
                out.write(buf, 0, i);
                i = in.read(buf);
            }
            return out.toByteArray();
        } finally {
            if (in != null) {
                in.close();
            }
        }
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
