package se.vgregion.messagebus;

import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * User: pabe
 * Date: 2011-05-10
 * Time: 16:12
 */
@ContextConfiguration(
        locations = {
                "/META-INF/message-bus-spring-test.xml", "/META-INF/camel-spring-test.xml"})
public class CamelRestComponentTest extends AbstractJUnit4SpringContextTests {

    @Test
    @DirtiesContext
    public void test() {
        
    }
}
