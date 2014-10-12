package se.vgregion.crypto.xml;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Bergström
 */
public class XmlSignerTest {

    @Test
    public void testSignAndVerifyXml() throws Exception {

        String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rootElement><content>theContent</content></rootElement>";

        XmlSigner signer = new XmlSigner();

        KeyStore.PrivateKeyEntry privateKeyEntry = getPrivateKeyEntry();

        String signedXml1 = signer.sign(xml1, privateKeyEntry);

        System.out.println(signedXml1);

        X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X509")
                .generateCertificate(this.getClass().getClassLoader().getResourceAsStream("testcert.pem"));

        boolean verify1 = signer.verify(signedXml1, certificate);

        assertTrue(verify1);
    }

    private KeyStore.PrivateKeyEntry getPrivateKeyEntry() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException {
        // Load the KeyStore and get the signing key and certificate.
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(this.getClass().getClassLoader().getResourceAsStream("teststore.jks"), "changeit".toCharArray());
        return (KeyStore.PrivateKeyEntry) keyStore.getEntry
                ("test_alias", new KeyStore.PasswordProtection("changeit".toCharArray()));
    }

    @Test
    public void testSignAnvValidateEnveloping() throws Exception {

        String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rootElement><content>theContent</content></rootElement>";

        XmlSigner signer = new XmlSigner();

        KeyStore.PrivateKeyEntry privateKeyEntry = getPrivateKeyEntry();

        String signedXml1 = signer.signEnveloping(xml1, privateKeyEntry);

        System.out.println(prettyFormat(signedXml1, 4));

        Validate.validate(signedXml1);
    }

    @Test
    public void testVerifyExampleMadeWithProductionMobileBankId() throws Exception {
        String signedXmlDSig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/>" +
                "<Reference Type=\"http://www.bankid.com/signature/v1.0.0/types\" URI=\"#bidSignedData\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/>" +
                "<DigestValue>s2brpRGaWum++bjDt+a2+t9n4HXapVnzMmUfBza9kG0=</DigestValue>" +
                "</Reference>" +
                "<Reference URI=\"#bidKeyInfo\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/>" +
                "<DigestValue>sGZnWb+xl/RzZw6ZII5hf0+vw7GXCk9Js8rH+vPDT9M=</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>" +
                "<SignatureValue>VJDGNA0Ydf9dtOgnv7B5JKJjgeUGlaHRvVUo0HXcrrSXmsqcFEEYCWGbA6Jh0hXTBOcd8b30FZX+zsHw/t4+oVYLbWy5Mjvw4Qg3QBgI1cyH63ev+1gqiuC4GIyjql/5Ul5Zc0hVLIE2wa7mMe5VzeG+aodi6zLJS42axmg1zml1jj4fo7JP2rY0EhAbx6dEUE1T2X+dY7azm1SZRoj3hE+1rhNgTpHphJajgLGExE8+kLLbUo+YhZEhqmNY8wcDAkUkz/OnLxmkYqxbWR8oJ44OYacwRC+T9YsnJWU9T/vXOwlBf0OgMeTi0BQCnLY627PnEu5AsQ5HebVQ7/pBBw==</SignatureValue>\n" +
                "<KeyInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\" Id=\"bidKeyInfo\">" +
                "<X509Data>" +
                "<X509Certificate>MIIFeTCCA2GgAwIBAgIIRcrxY3cvBc0wDQYJKoZIhvcNAQELBQAwfTELMAkGA1UEBhMCU0UxMDAu\n" +
                "BgNVBAoMJ1NrYW5kaW5hdmlza2EgRW5za2lsZGEgQmFua2VuIEFCIChwdWJsKTETMBEGA1UEBRMK\n" +
                "NTAyMDMyOTA4MTEnMCUGA1UEAwweU0VCIEN1c3RvbWVyIENBMyB2MSBmb3IgQmFua0lEMB4XDTE0\n" +
                "MDYyODIyMDAwMFoXDTE3MDYyODIxNTk1OVowgdMxCzAJBgNVBAYTAlNFMTAwLgYDVQQKDCdTa2Fu\n" +
                "ZGluYXZpc2thIEVuc2tpbGRhIEJhbmtlbiBBQiAocHVibCkxEzARBgNVBAQMCkJFUkdTVFLDlk0x\n" +
                "DzANBgNVBCoMBlBBVFJJSzEVMBMGA1UEBRMMMTk4MzAxMjQ0ODEzMTkwNwYDVQQpDDAoMTQwNjI5\n" +
                "IDIyLjQwKSBQQVRSSUsgQkVSR1NUUsOWTSAtIE1vYmlsdCBCYW5rSUQxGjAYBgNVBAMMEVBBVFJJ\n" +
                "SyBCRVJHU1RSw5ZNMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg0pNhSAJeg1DHFA+\n" +
                "eF2yc9oSDPD8sn36l1PIzZOiyHW//EnXO+RPJYykVhjnxmQSKCGL7w7UQKzwFdI6ttlRQrcvby8f\n" +
                "et/oL9UVo7Y2pIPBBaW7lz896WpC095jmV7q2ScEmflrx4aH2iVvZDlPU6k4FdvGQ0oEX4+27V6r\n" +
                "nIazUjjWQKyS1craqSlVDtfhbB4yyPQgfNlPsMz8OBCvHBWnWv0hjGzeaItqt8VmpXgxYPlynsvy\n" +
                "jOowgECBqOePyLcEGTY7H+bBdH0q5Tl6rheKV+nY3P9oiuTYj6BuLzokejb9fwv5fq63ZWc8s02B\n" +
                "WzU8Z8S7RKyHPjDHh/sMlQIDAQABo4GlMIGiMDsGCCsGAQUFBwEBBC8wLTArBggrBgEFBQcwAYYf\n" +
                "aHR0cDovL29jc3AucmV2b2NhdGlvbnN0YXR1cy5zZTATBgNVHSAEDDAKMAgGBiqFcE4BBTAOBgNV\n" +
                "HQ8BAf8EBAMCBkAwHQYDVR0OBBYEFB/QDZy6lsnxHbyUQNKTCwGnHS+6MB8GA1UdIwQYMBaAFIcs\n" +
                "AY3gqr5Nl+3jJn2A7vJYw5Y5MA0GCSqGSIb3DQEBCwUAA4ICAQABb5X5ZXaUd9zGgXWLoc2Mydni\n" +
                "0gU0+UXdLq/R/5+euGoeOMWKzsj2dHXNpR/6vz6MD1F1+olIIRc1zEOup5rIqEPiekotXXgOaWpD\n" +
                "xE8oPiGmlz8C4589g3RwrYLZtKgvmOEaQPwDAOSzxPTMAwkvkhVVoXIwLfq1PuPPcfRnBZhAG+tN\n" +
                "5OBPfkNcPKBm8L1nVEIJl34Q3bIx5Hg3K3I76e/1lzy+Ov2HsCjUldI1erC3h4dP6cI0EC3kxaGd\n" +
                "p9PWsW4DE7JQ9NpIhg5mRoedvzmSK5NV27H5i3g7UBOvR2yJ8cpTo/K3FGWE6//fpjblOmoMM+gF\n" +
                "YSC15FP5CosUY1TEGVx2pz5ft/QjTTa2SlLZ0O9km/hMch+36qD/cVs7TIpnJUY/7LwNwSwErWrP\n" +
                "jrQPT2XRkFLpqm2eSLu6XhVClwNmos3FrLhFyxOl2m1PF8RBBCTzC6mzJGjE/NxCZtjRq8WWcBJU\n" +
                "ZAaKrjJJ8zu4K3tQEow9ocnzjvSuTBSysk4yL1OexG/eCF17mLKBknjLMG80F0AI6q5JRzwlCiZ4\n" +
                "SICN5hpgxZCRVfFxmp43fB+Gb6MkhvzC9VtBLzItZGVXzrOrEnkH5cOaIioZW56ecuuFhCN690kz\n" +
                "8m9mwBaYERnBUuDCSSt0N67FWobTGAxcV2HWv9G1qKeAuq/NqA==</X509Certificate>" +
                "<X509Certificate>MIIF6jCCA9KgAwIBAgIIXRFNBHoA9UkwDQYJKoZIhvcNAQENBQAwczELMAkGA1UEBhMCU0UxMDAu\n" +
                "BgNVBAoMJ1NrYW5kaW5hdmlza2EgRW5za2lsZGEgQmFua2VuIEFCIChwdWJsKTETMBEGA1UEBRMK\n" +
                "NTAyMDMyOTA4MTEdMBsGA1UEAwwUU0VCIENBIHYxIGZvciBCYW5rSUQwHhcNMTIwNjA0MTIxNTM2\n" +
                "WhcNMzQxMjAxMTIxNTM2WjB9MQswCQYDVQQGEwJTRTEwMC4GA1UECgwnU2thbmRpbmF2aXNrYSBF\n" +
                "bnNraWxkYSBCYW5rZW4gQUIgKHB1YmwpMRMwEQYDVQQFEwo1MDIwMzI5MDgxMScwJQYDVQQDDB5T\n" +
                "RUIgQ3VzdG9tZXIgQ0EzIHYxIGZvciBCYW5rSUQwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIK\n" +
                "AoICAQDCEOKe/asHrD8e4kzj7Sjf6CnFUoIjL98zmX4/0wASLRFvu89bJTl09d2n/dsHiXJWqHWD\n" +
                "6jD/4ldYSOxGqUokULIc5yhGiJO9IU8BW2LTtQAxGVLU63yZgg8IdQP+EdBbVo7hQG/AmKfarCK/\n" +
                "159bDOzyYEPXgqzbgVb+fo+EYh9jBS9xLepcs5zIYlgMDprcBTsuvGXdhHzwExtquUFaVYr7PZYa\n" +
                "8seny4iUkIzuLQvaxl8M3pBlStFKqUzCS7t1F3OzCZX87/pAXWWEEA6uUlbQIn2mVFM/egw6sVUd\n" +
                "hwzopPSTch7Wl/LyvDg94HNwo7SjQnBwWFimfvXiF5ORwZIe6ruuiFg1zPF75JsktGOAjiS2ovcL\n" +
                "bBsGVmIyfjISxvYccwir2PcFS+TWuf5AMiln4ei/0zHOrmL4ZDJbXWvITjacKQ5tMCI/MYlkgAK1\n" +
                "0WJXc5JbXbWzaQdYIBEZ5luHIsuiSf1nXTMZCL0RhXk8m4YSGSDOnG58es34XvGHuxAkTk5blPlT\n" +
                "bIBPEAZ5CHI19n4IrDpvaGADD9EJmXvnKG1/QDFmR5cI4sYILFiDApiF/ge3EYg7VqX4ox/NTsn9\n" +
                "qL/ijkBTFJvJOiu+kOLSb4PeZmsANSwdbHo6pwRdbEB7ad+oRbb3NqRifhs2j9TqvR07bDrXNdUK\n" +
                "kL0CMQIDAQABo3gwdjAdBgNVHQ4EFgQUhywBjeCqvk2X7eMmfYDu8ljDljkwDwYDVR0TAQH/BAUw\n" +
                "AwEB/zAfBgNVHSMEGDAWgBTfDMAH38OupdZ6y4QKL7u6PDXMUzATBgNVHSAEDDAKMAgGBiqFcE4B\n" +
                "BTAOBgNVHQ8BAf8EBAMCAQYwDQYJKoZIhvcNAQENBQADggIBABNnbTJDJ4hf3KSPldiYsyTWHP/E\n" +
                "laamzf9tnXGsZAz+M4oLf1uk1XZVZR5GSiX7qIYTe5uIDN6xx+JI/8cFYqbyM3c2k5B82VI83qrI\n" +
                "w3YXQEo1C2oPvejESJoXJgq2w3VMzOR11tTTfBQPTDnBTeDlkXt+uPqyoHy7DxJ6KMj8Z/NX2Q6+\n" +
                "4vSYBwkKNXDLV/0pOn9Ev2ORP5q9Me29g8pqWyVgHWO7fhUImE75TvoTl0p5xr4HhUI0eBwqBYsh\n" +
                "CV7HbyEOc0lVt33FuVnIiTCYMQA0fMwc3KV8H4Fid8zxnQIVE0RtRe7EjF9vdNRBFmCOkmAvWplp\n" +
                "hzRhNHCOFceSFBATxlZbI0MorH2+OfwuxqmIbHuZBZfn/MBdzaw2VltAg2IU8dfamkP3ZxjY7i0j\n" +
                "cBN8haq1K9HJGcnv2joB5vQXcZptUciDLFSHhGhoHVFV1FJfAZQC/1TA9F/qe33JaQc6t8RK19td\n" +
                "pT/VdhqkcG/Lk2Oh5Zp5kBzLsZEfXO3cIgAjIPm1uvf7zpPN9fUowYLVK2bUnLO4pFChy7+213Gn\n" +
                "63s43dN/ubuKaCl/Nu2VZjfZFqWrbNI6g1bZqJ1mIE5AcRlGRN9R2kWU5rnORPFkZapx/dEPVAF5\n" +
                "Cu7xritxQSlpbPqLjAfaw4ZcM53DEF7gf/wGALdjGwwf6lf0</X509Certificate>" +
                "<X509Certificate>MIIF0DCCA7igAwIBAgIIO/VkFE29dqwwDQYJKoZIhvcNAQENBQAwYzEkMCIGA1UECgwbRmluYW5z\n" +
                "aWVsbCBJRC1UZWtuaWsgQklEIEFCMR8wHQYDVQQLDBZCYW5rSUQgTWVtYmVyIEJhbmtzIENBMRow\n" +
                "GAYDVQQDDBFCYW5rSUQgUm9vdCBDQSB2MTAeFw0xMjA2MDQxMTM2MjdaFw0zNDEyMzExMTM2Mjda\n" +
                "MHMxCzAJBgNVBAYTAlNFMTAwLgYDVQQKDCdTa2FuZGluYXZpc2thIEVuc2tpbGRhIEJhbmtlbiBB\n" +
                "QiAocHVibCkxEzARBgNVBAUTCjUwMjAzMjkwODExHTAbBgNVBAMMFFNFQiBDQSB2MSBmb3IgQmFu\n" +
                "a0lEMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAm/zJeRPxFnQjyh2C7GoZCIjvnk/V\n" +
                "3Paockj2imZ0pHpcjyc0AfM+AZDYX6PAnqlPb9sh31W3xfsXScWqcnKEbEbgztZL65quacwgbAaS\n" +
                "ZAN4ljLZfK4hQu/cZDZPBPXRRz49EgAYRx3J2c0HkMpTg6wvg2pi99PeEi608uPqzSOJT3UOEZXd\n" +
                "JjPdya4Ql03R2xl3q2pmERpk8tVJLyn1UJRN9mNpL692wLVVIy4MiT4ApQsVqmi2JupVyVYIh1IV\n" +
                "xUtjb1R0mmgPpUmuNyMg2lgNZvd5D37xZR5+m8ZFnJEqeqxce5AFmabxqewJQP/dSdVXOMja2XrY\n" +
                "b3BwVArqfvHtTFKUxrQlaExItNRsmUH4764owlQomfStifOx+p/iMOkkmvRV+GDMdb+DslMP9UxS\n" +
                "wwrozmbIMq0OfoUxlPQop0h/UXUN91i51jWxNMcZkwEN5/lAiD9TpHvQwmf4c/zFd4r6/LsFYqdw\n" +
                "dYVmFnppZFA04h2qsHGjKvxsfvaA4nso/eOPQckvwaQDVOZ3TWw83AllycAufKqSJ3P2+CwcRdjF\n" +
                "2HrLRYEmGYaN9LZE1Gw/8/l7EA1NLZQbfUJ0jesa5m0oDXlnajjq2uJ1ZkYwXlk+SZaP222eYzW8\n" +
                "kHHW1h1ELxP3y5o3skMkx1KrKmanxnPrfeN7N5lKoI5lzw8CAwEAAaN4MHYwHQYDVR0OBBYEFN8M\n" +
                "wAffw66l1nrLhAovu7o8NcxTMA8GA1UdEwEB/wQFMAMBAf8wHwYDVR0jBBgwFoAUZ4q6supIHHr1\n" +
                "O2g3J3IG65Fjy1MwEwYDVR0gBAwwCjAIBgYqhXBOAQEwDgYDVR0PAQH/BAQDAgEGMA0GCSqGSIb3\n" +
                "DQEBDQUAA4ICAQBT1YhWadaDi3PYPCKw8AHdd/cTLoOfxatW0HRtiLZOowMQOmQ4sKvFMEVnyH+P\n" +
                "ojRSr5XyyHAKeP6SE1HUZPgQBkcqgmhmS68jHqY8+JAm1d07cretMjBNbEJ0iBvxClJWVfC9ZSOj\n" +
                "CNWMtvuPFYu0O9VEhXmJkd6Wlaw0zeHpxYb/Cex+C4z9/4OcdRdDMQZRfnNHVQF50POq+o0DMiHo\n" +
                "TeZ8LqLQnW7SZycqY5h3XZjhUgRrxGdchqzIMLbrGgA2dCQDQdHsvtoRUraFKq+XaXSDAl7Hr/Hj\n" +
                "VWyYhjrs/ad+YqjBdZ51zxf5JbgOkhSmuC9QwCn+ARCURCo2CC5KTstDuH8UMZCiX0lHsKO/YTU7\n" +
                "T0M/8Kl/62JT6BH16u4mOC+kAxuAVqG4dq3cBpVq3YoENdu82taexhmMpFa7DDbRzscJ6nFZvymZ\n" +
                "MdwJctpxKj4oSgj92lyRFyTmoo9I6K4qWxKVel7QeF0HJoYpSIiSEYud8Pn+4kWe2WzbgZeOVRf/\n" +
                "pj8i7COXpjDRl2yTn7x8AD79w+AXQN98JBidPX8vGgP6zru+pvJM7/x2suK/+VhBAVCpTfU2vN6p\n" +
                "hiVO+udPskcGcG0bYK2Eg19txgCpVQA4u4cufmkApIHS3bVJ6jtwk1UBb6JQb2JsoiNP7x3mw9dH\n" +
                "GPaDrL6ZUPeDAQ==</X509Certificate>" +
                "</X509Data>" +
                "</KeyInfo>" +
                "<Object>" +
                "<bankIdSignedData xmlns=\"http://www.bankid.com/signature/v1.0.0/types\" Id=\"bidSignedData\">" +
                "<usrVisibleData charset=\"UTF-8\" visible=\"wysiwys\">SGVq</usrVisibleData>" +
                "<srvInfo>" +
                "<name>Q049TG9naWNhIFN2ZXJpZ2UgQUItVsOkc3RyYSBHw7Z0YWxhbmRzcmVnaW9uZW4tMDEsIE9JRC4yLjUuNC40MT1Ww6RzdHJhIEfDtnRhbGFuZHNyZWdpb25lbiwgU0VSSUFMTlVNQkVSPTU1NjMzNzIxOTEsIE89U3ZlbnNrYSBIYW5kZWxzYmFua2VuIEFCIChwdWJsKSwgQz1TRQ==</name>" +
                "<nonce>Rvqo60XSrl8XHp1wa7VsEZMogEw=</nonce>" +
                "</srvInfo>" +
                "<clientInfo>" +
                "<funcId>Signing</funcId>" +
                "<version>Ni4yLjA=</version>" +
                "<env>" +
                "<ai>" +
                "<type>QU5EUk9JRA==</type>" +
                "<deviceInfo>c2Ftc3VuZyxrbHRlLFNNLUc5MDBGLDE5</deviceInfo>" +
                "<uhi>fL2Z9KSYfRhSIsT8qEuX5agU5tM=</uhi>" +
                "</ai>" +
                "</env>" +
                "</clientInfo>" +
                "</bankIdSignedData>" +
                "</Object>" +
                "</Signature>";

        assertTrue(Validate.validate(signedXmlDSig));
    }

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

}
