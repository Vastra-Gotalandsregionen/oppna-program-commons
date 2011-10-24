package se.vgregion.ssl;

import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Patrik Bergström
 */
public class HttpsClientConfigurer implements HttpClientConfigurer {

    private String trustStore;
    private String trustStorePassword;
    private String keyStore;
    private String keyStorePassword;

    public HttpsClientConfigurer(String trustStore, String trustStorePassword, String keyStore, String keyStorePassword) {
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
        this.keyStore = keyStore;
        this.keyStorePassword = keyStorePassword;
    }

    @Override
    public void configureHttpClient(HttpClient client) {
        //see http://camel.465427.n5.nabble.com/Using-HTTPS-in-camel-http-when-remote-side-has-self-signed-cert-td473876.html
        ProtocolSocketFactory socketFactory = new SecureProtocolSocketFactory() {
            @Override
            public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException, UnknownHostException {
                return createSocket(s, i, null, 0, null);
            }

            @Override
            public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
                return createSocket(s, i, null, 0, null);
            }

            @Override
            public Socket createSocket(String s, int i, InetAddress inetAddress, int i1, HttpConnectionParams httpConnectionParams) throws IOException, UnknownHostException, ConnectTimeoutException {
                try{
                    SSLSocketFactory sslSocketFactory = initSslSocketFactory();
                    return sslSocketFactory.createSocket(s, i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
                return createSocket(s, i, null, 0, null);
            }
        };

        Protocol.registerProtocol("https", new Protocol("https", socketFactory, 443));
    }

    private SSLSocketFactory initSslSocketFactory() throws Exception {
        ConvenientSslContextFactory factory = new ConvenientSslContextFactory(trustStore, trustStorePassword,
                keyStore, keyStorePassword);
        return factory.createSslContext().getSocketFactory();
    }
}
