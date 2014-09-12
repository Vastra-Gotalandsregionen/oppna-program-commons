package se.vgregion.certificate;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.X509Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.provider.certpath.PKIXCertPathValidator;
import sun.security.provider.certpath.X509CertPath;

import javax.naming.NamingException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.security.*;
import java.security.cert.*;
import java.util.*;

public class PkixUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PkixUtil.class);

    public static KeyStore.PrivateKeyEntry getPrivateKeyEntry(InputStream keystoreInput, String storeType, String alias,
                                                  String password) {
        try {

            KeyStore keyStore = KeyStore.getInstance(storeType);
            keyStore.load(keystoreInput, password.toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry
                    (alias, new KeyStore.PasswordProtection(password.toCharArray()));
            return privateKeyEntry;

        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableEntryException e) {
            throw new RuntimeException(e);
        }
    }

    public static void validateCertificate(X509Certificate trustedCert) throws CertificateException {
        /*try {
            validateCertificate2(cert);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (CertPathValidatorException e) {
            e.printStackTrace();
        }*/
        trustedCert.checkValidity();

        verifyCertificateCRLs(trustedCert);
    }

    public static void validateCertificate4(X509Certificate cert) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, CertPathValidatorException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.setCertificateEntry("anAlias", cert);
        trustManagerFactory.init(keyStore);

        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        ServerSocket serverSocket = sslContext.getServerSocketFactory().createServerSocket(8800);

        serverSocket.accept();

        cert.checkValidity();

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        List<X509Certificate> certificates = new ArrayList<X509Certificate>();
        certificates.add(cert);
        CertPath certPath = certificateFactory.generateCertPath(certificates);

        FileInputStream keystoreStream = new FileInputStream("C:\\java\\workspace\\secrets\\certifikat\\pdl.portalen-test.vgregion.se_trust.jks");
        KeyStore anchors = KeyStore.getInstance(KeyStore.getDefaultType());
        anchors.load(keystoreStream, "asdf".toCharArray());
//        TrustAnchor trustAnchor = new TrustAnchor(cert, null);

        PKIXParameters params = new PKIXParameters(anchors);
        params.setRevocationEnabled(true);
        CertPathValidator cpv = CertPathValidator.getInstance(CertPathValidator.getDefaultType());
        PKIXCertPathValidatorResult pkixCertPathValidatorResult = (PKIXCertPathValidatorResult) cpv.validate(certPath, params);

    }
    public static void validateCertificate3(X509Certificate cert) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, CertPathValidatorException {

        cert.checkValidity();

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        List<X509Certificate> certificates = new ArrayList<X509Certificate>();
        certificates.add(cert);
        CertPath certPath = certificateFactory.generateCertPath(certificates);

        FileInputStream keystoreStream = new FileInputStream("C:\\java\\workspace\\secrets\\certifikat\\pdl.portalen-test.vgregion.se_trust.jks");
        KeyStore anchors = KeyStore.getInstance(KeyStore.getDefaultType());
        anchors.load(keystoreStream, "asdf".toCharArray());
//        TrustAnchor trustAnchor = new TrustAnchor(cert, null);

        PKIXParameters params = new PKIXParameters(anchors);
        params.setRevocationEnabled(true);
        CertPathValidator cpv = CertPathValidator.getInstance(CertPathValidator.getDefaultType());
        PKIXCertPathValidatorResult pkixCertPathValidatorResult = (PKIXCertPathValidatorResult) cpv.validate(certPath, params);

    }

    public static void validateCertificate2(X509Certificate cert) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, CertPathValidatorException {
//        KeyStore keyStore = KeyStore.getInstance("JKS");
        FileInputStream keystoreStream = new FileInputStream("C:\\java\\workspace\\secrets\\rp-certifikat\\pdl.portalen-test.vgregion.se_trust.jks");
//        InputStream keystoreStream = CertificateUtil.class.getClassLoader().getResourceAsStream("teststore.jks");

        InputStream trustStoreInput = keystoreStream;
        char[] password = "asdf".toCharArray();
        List<X509Certificate> chain = Arrays.asList(cert);

        KeyStore anchors = KeyStore.getInstance(KeyStore.getDefaultType());
        anchors.load(trustStoreInput, password);
        X509CertSelector target = new X509CertSelector();
        target.setCertificate(chain.get(0));
        PKIXBuilderParameters params = new PKIXBuilderParameters(anchors, target);

        PKIXCertPathValidator validator = new PKIXCertPathValidator();
        CertPathValidatorResult certPathValidatorResult = validator.engineValidate(new X509CertPath(Arrays.asList(
                anchors.getCertificate("SITHS_ROOT_CA_V1_PP"),
                anchors.getCertificate("siths_type_3_ca_v1pp"),
                cert)), params);

    }

    public static void verifyCertificateCRLs(X509Certificate cert) throws CertificateException {
        try {
            List<String> crlDistPoints = getCrlDistributionPoints(cert);
            for (String crlDP : crlDistPoints) {
                X509CRL crl = downloadCRL(crlDP);
                if (crl.isRevoked(cert)) {
                    throw new CertificateException("The certificate is revoked by CRL: " + crlDP);
                }
            }
        } catch (Exception ex) {
            if (ex instanceof CertificateException) {
                throw (CertificateException) ex;
            } else {
                throw new CertificateException("Can not verify CRL for certificate: " + cert.getSubjectX500Principal());
            }
        }
    }

    /**
     * Extracts all CRL distribution point URLs from the "CRL Distribution Point"
     * extension in a X.509 certificate. If CRL distribution point extension is
     * unavailable, returns an empty list.
     */
    public static List<String> getCrlDistributionPoints(
            X509Certificate cert) throws CertificateParsingException, IOException {
        byte[] crldpExt = cert.getExtensionValue(
                X509Extension.cRLDistributionPoints.getId());
        if (crldpExt == null) {
            List<String> emptyList = Collections.emptyList();
            return emptyList;
        }
        ASN1InputStream oAsnInStream = new ASN1InputStream(
                new ByteArrayInputStream(crldpExt));
        DERObject derObjCrlDP = oAsnInStream.readObject();
        DEROctetString dosCrlDP = (DEROctetString) derObjCrlDP;
        byte[] crldpExtOctets = dosCrlDP.getOctets();
        ASN1InputStream oAsnInStream2 = new ASN1InputStream(new ByteArrayInputStream(crldpExtOctets));
        DERObject derObj2 = oAsnInStream2.readObject();
        CRLDistPoint distPoint = CRLDistPoint.getInstance(derObj2);
        List<String> crlUrls = new ArrayList<String>();
        for (DistributionPoint dp : distPoint.getDistributionPoints()) {
            DistributionPointName dpn = dp.getDistributionPoint();
            // Look for URIs in fullName
            if (dpn != null) {
                if (dpn.getType() == DistributionPointName.FULL_NAME) {
                    GeneralName[] genNames = GeneralNames.getInstance(
                            dpn.getName()).getNames();
                    // Look for an URI
                    for (int j = 0; j < genNames.length; j++) {
                        if (genNames[j].getTagNo() == GeneralName.uniformResourceIdentifier) {
                            String url = DERIA5String.getInstance(
                                    genNames[j].getName()).getString();
                            crlUrls.add(url);
                        }
                    }
                }
            }
        }
        return crlUrls;
    }

    /**
     * Downloads CRL from given URL. Supports http, https, ftp and ldap based
     * URLs.
     */
    private static X509CRL downloadCRL(String crlURL) throws IOException, CertificateException, CRLException,
            NamingException {
        if (crlURL.startsWith("http://") || crlURL.startsWith("https://") || crlURL.startsWith("ftp://")) {
            return downloadCRLFromWeb(crlURL);
        } else if (crlURL.startsWith("ldap://")) {
            LOGGER.warn("Certificate revocation URL has ldap protocol which is not supported. Cannot verify CRL.");
//            return downloadCRLFromLDAP(crlURL); todo implement this
            throw new CertificateException("Can not download CRL from certificate distribution point: " + crlURL);
        } else {
            throw new CertificateException("Can not download CRL from certificate distribution point: " + crlURL);
        }
    }

    /**
     * Downloads a CRL from given HTTP/HTTPS/FTP URL, e.g.
     * http://crl.infonotary.com/crl/identity-ca.crl
     */
    private static X509CRL downloadCRLFromWeb(String crlURL) throws MalformedURLException,
            IOException, CertificateException,
            CRLException {
        URL url = new URL(crlURL);
        InputStream crlStream = url.openStream();
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509CRL) cf.generateCRL(crlStream);
        } finally {
            crlStream.close();
        }
    }

    /**
     * Downloads a CRL from given LDAP url, e.g.
     * ldap://ldap.infonotary.com/dc=identity-ca,dc=infonotary,dc=com
     */
    /*private static X509CRL downloadCRLFromLDAP(String ldapURL) throws CertificateException,
            NamingException, CRLException {
        Map<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapURL);

        DirContext ctx = new InitialDirContext((Hashtable)env);
        Attributes avals = ctx.getAttributes("");
        Attribute aval = avals.get("certificateRevocationList;binary");
        byte[] val = (byte[]) aval.get();
        if ((val == null) || (val.length == 0)) {
            throw new CertificateException(
                    "Can not download CRL from: " + ldapURL);
        } else {
            InputStream inStream = new ByteArrayInputStream(val);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509CRL) cf.generateCRL(inStream);
        }
    }*/

}
