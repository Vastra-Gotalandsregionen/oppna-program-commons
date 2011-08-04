package se.vgregion.messagebus.util;

import com.liferay.portal.kernel.messaging.MessageBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: David Rosell
 * Date: 4/8-11
 * Time: 10:27
 */
public class JaxbUtil {
    private static final Logger logger = LoggerFactory.getLogger(JaxbUtil.class);

    private String contextNameSpace;

    public JaxbUtil(String contextNamespace) {
        this.contextNameSpace = contextNamespace;
    }

    public <T> T unmarshal(String xml) {
        try {
            JAXBContext jc = JAXBContext.newInstance(contextNameSpace);
            //Create marshaller
            Unmarshaller m = jc.createUnmarshaller();
            //Marshal object into file.
            return (T) m.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }

    public <T> String marshal(T target) {
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jc = JAXBContext.newInstance(contextNameSpace);
            //Create marshaller
            Marshaller m = jc.createMarshaller();
            //Marshal object into file.
            m.marshal(target, sw);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
        return sw.toString();
    }

    public <T> T extractResponse(Object response) throws MessageBusException {
        if (response instanceof Exception) {
            throw new MessageBusException((Exception) response);
        } else if (response instanceof String) {
            logger.info("Response: " + response);
            return (T)unmarshal((String) response);
        } else {
            throw new MessageBusException("Unknown response type: " + response.getClass());
        }
    }
}
