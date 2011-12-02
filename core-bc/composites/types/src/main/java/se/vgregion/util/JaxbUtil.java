package se.vgregion.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Utility class for marshalling and unmarshalling with JAXB.
 * <p/>
 * @author David Rosell
 * @author Patrik Bergström
 */
public class JaxbUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxbUtil.class);

    private JAXBContext jaxbContext;

    /**
     * Constructor.
     *
     * @param contextNamespace the package name where the JAXB classes are located
     */
    public JaxbUtil(String contextNamespace) throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(contextNamespace);
    }

    /**
     * Constructor.
     *
     * @param classes the classes which are to be marshalled/unmarshalled
     */
    public JaxbUtil(Class... classes) throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(classes);
    }

    /**
     * Generic method for unmarshalling an XML <code>String</code> to an <code>Object</code>.
     *
     * @param xml xml
     * @param <T> <code>T</code>
     * @return <code>T</code>
     */
    public <T> T unmarshal(String xml) {
        try {
            //Create marshaller
            Unmarshaller m = jaxbContext.createUnmarshaller();
            //Marshal object into file.
            return (T) m.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }

    /**
     * Generic method for marshalling an <code>Object</code> to a <code>String</code>.
     *
     * @param target target
     * @param <T>    <code>T</code>
     * @return <code>T</code>
     */
    public <T> String marshal(T target) {
        StringWriter sw = new StringWriter();
        try {
            //Create marshaller
            Marshaller m = jaxbContext.createMarshaller();
            //Marshal object into file.
            m.marshal(target, sw);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
        return sw.toString();
    }

}
