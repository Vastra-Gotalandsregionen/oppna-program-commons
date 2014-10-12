package se.vgregion.crypto.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import se.vgregion.certificate.PkixUtil;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmlSigner {

    public String signEnveloping(String xml, KeyStore.PrivateKeyEntry privateKeyEntry) {

        try {
            // First, create the DOM XMLSignatureFactory that will be used to
            // generate the XMLSignature
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            // Next, create a Reference to a same-document URI that is an Object
            // element and specify the SHA1 digest algorithm
            String referenceId = "documentId";
            Reference ref = fac.newReference("#" + referenceId, fac.newDigestMethod(DigestMethod.SHA256, null),
                    Collections.singletonList(
                            fac.newTransform(CanonicalizationMethod.INCLUSIVE, (TransformParameterSpec) null)),
                    null, null);


            // Next, create the referenced Object
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

            DOMStructure domStructure = new DOMStructure(doc.getDocumentElement());
            XMLObject theXmlInputObject = fac.newXMLObject(Collections.singletonList(domStructure), referenceId, null,
                    null);

            // Create the SignedInfo
            SignedInfo si = fac.newSignedInfo(
                    fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null),
//                    fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(ref));

            // Create the XMLSignature (but don't sign it yet)
            XMLSignature signature = fac.newXMLSignature(si, getKeyInfo(privateKeyEntry),
                    Collections.singletonList(theXmlInputObject), null, null);

            // Create a DOMSignContext and specify the DSA PrivateKey for signing
            // and the document location of the XMLSignature
            DOMSignContext dsc = new DOMSignContext(privateKeyEntry.getPrivateKey(), doc);

            // Lastly, generate the enveloping signature using the PrivateKey
            signature.sign(dsc);

            ByteArrayOutputStream returnBytes = new ByteArrayOutputStream();

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc), new StreamResult(returnBytes));

            return returnBytes.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String sign(String xml, KeyStore.PrivateKeyEntry privateKeyEntry) {
        return sign(xml, privateKeyEntry, Transform.ENVELOPED);
    }

    public String sign(String xml, KeyStore.PrivateKeyEntry privateKeyEntry, String transform) {
        try {
            // Create a DOM XMLSignatureFactory that will be used to
            // generate the enveloped signature.
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            // Create a Reference to the enveloped document (in this case,
            // you are signing the whole document, so a URI of "" signifies
            // that, and also specify the SHA1 digest algorithm and
            // the Transform method.
            Reference reference = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(fac.newTransform(transform, (TransformParameterSpec) null)),
                    null, null);

            // Create the SignedInfo.
            SignedInfo signedInfo = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
                            (C14NMethodParameterSpec) null), fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(reference));
            
            KeyInfo keyInfo = getKeyInfo(privateKeyEntry);

            // Instantiate the document to be signed.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

            // Create a DOMSignContext and specify the RSA PrivateKey and
            // location of the resulting XMLSignature's parent element.
            DOMSignContext domSignContext = new DOMSignContext
                    (privateKeyEntry.getPrivateKey(), doc.getDocumentElement());

            // Create the XMLSignature, but don't sign it yet.
            XMLSignature signature = fac.newXMLSignature(signedInfo, keyInfo);

            // Marshal, generate, and sign the enveloped signature.
            signature.sign(domSignContext);

            // Output the resulting document.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(outputStream));

            return outputStream.toString("UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private KeyInfo getKeyInfo(KeyStore.PrivateKeyEntry privateKeyEntry) {
        // Get the certificate from the private key.
        X509Certificate cert = (X509Certificate) privateKeyEntry.getCertificate();

        // Create the KeyInfo containing the X509Data.
        KeyInfoFactory keyInfoFactory = KeyInfoFactory.getInstance();
//        KeyInfoFactory keyInfoFactory = fac.getKeyInfoFactory();
        List x509Content = new ArrayList();
        x509Content.add(cert.getSubjectX500Principal().getName());
        x509Content.add(cert);
        X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
        return keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
    }

    public boolean verify(String signedXml, X509Certificate trustedCertificate) {

        try {
            // Create a DOM XMLSignatureFactory that will be used to
            // generate the enveloped signature.
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

            // Instantiate the document to be verified.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(signedXml.getBytes("UTF-8")));

            setIdAttributesAsId(doc);

            // Find Signature element.
            NodeList nodeList = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
            if (nodeList.getLength() == 0) {
                throw new XmlSignerException("Cannot find Signature element.");
            }

            // Verify it hasn't been revoked
            PkixUtil.validateCertificate(trustedCertificate);

            // Create a DOMValidateContext and specify document context.
            PublicKey publicKey = trustedCertificate.getPublicKey();
            DOMValidateContext valContext = new DOMValidateContext(publicKey, nodeList.item(0));

            // Unmarshal the XMLSignature.
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);

            // Validate the XMLSignature.
            boolean coreValidity = signature.validate(valContext);

            return coreValidity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setIdAttributesAsId(Document doc) {
        NodeList elementsByTagName = doc.getElementsByTagName("bankIdSignedData");
        if (elementsByTagName.getLength() > 0) {
            Element bankIdSignedDataElement = (Element) elementsByTagName.item(0);
            bankIdSignedDataElement.setIdAttribute("Id", true);
        }
    }

}
