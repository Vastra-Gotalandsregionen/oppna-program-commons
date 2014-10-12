package se.vgregion.certificate;

import org.junit.Test;

import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;

public class PkixUtilTest {

    @Test
    public void testBase64ToCertificate() throws Exception {
        String base64EncodedCertificate = "MIIFeTCCA2GgAwIBAgIIRcrxY3cvBc0wDQYJKoZIhvcNAQELBQAwfTELMAkGA1UEBhMCU0UxMDAu\n" +
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
                "8m9mwBaYERnBUuDCSSt0N67FWobTGAxcV2HWv9G1qKeAuq/NqA==";

        X509Certificate x509Certificate = PkixUtil.base64ToCertificate(base64EncodedCertificate);

        System.out.println(x509Certificate);

        String canonicalName = x509Certificate.getSubjectX500Principal().getName("CANONICAL");

        System.out.println(canonicalName);

        String start = canonicalName.substring(0, 20);
        assertEquals("cn=patrik bergström", start);
    }
}