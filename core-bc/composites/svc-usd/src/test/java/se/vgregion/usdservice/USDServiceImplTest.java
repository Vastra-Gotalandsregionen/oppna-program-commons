package se.vgregion.usdservice;

import org.junit.Test;
import se.vgregion.usdservice.domain.Issue;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Patrik Bergström
 */
public class USDServiceImplTest {

    @Test
    public void testParseIssues() throws Exception {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("UDSObjectList.xml");

        BufferedInputStream bis = new BufferedInputStream(resourceAsStream);

        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int n;
        while ((n = bis.read(buf)) > 0) {
            sb.append(new String(buf, 0, n));
        }

        String xml = sb.toString();

        xml = xml.replace("&", "&amp;");

        List<Issue> issues = USDServiceImpl.parseIssues(xml, "", "");

        assertEquals(2, issues.size());
    }

}
