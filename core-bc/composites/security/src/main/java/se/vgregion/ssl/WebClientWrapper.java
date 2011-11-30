package se.vgregion.ssl;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.SSLContext;

/**
 * Use this class to wrap an {@link HttpClient} to enable encrypted communication.
 */
public class WebClientWrapper {
    /**
     * Wrap a client by providing a {@link ConvenientSslContextFactory}.
     *
     * @param base the client to be wrapped
     * @param factory a factory with given SSL/TLS-related parameters set
     * @return the wrapped {@link HttpClient}
     */
    public static HttpClient wrapClient(HttpClient base, ConvenientSslContextFactory factory) {
        try {
            SSLContext ctx = factory.createSslContext();

            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
