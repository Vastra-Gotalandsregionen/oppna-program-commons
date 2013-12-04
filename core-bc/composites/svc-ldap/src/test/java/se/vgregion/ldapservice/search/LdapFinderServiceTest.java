package se.vgregion.ldapservice.search;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ldap.core.LdapTemplate;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Test for LdapFinderService.
 */
public class LdapFinderServiceTest {

    LdapFinderService finder;
    LdapTemplate template;
    @Before
    public void setUp(){
        finder = new LdapFinderService();
        template = Mockito.mock(LdapTemplate.class);
        finder.setLdapTemplate(template);
    }

    @Test
    public void find() {
        KivPerson kp = new KivPerson();
        kp.setCn("foo");
        finder.find(kp);
    }

    @Test
    public void findFuture() {
        KivPerson kp = new KivPerson();
        kp.setCn("foo");
        finder.findFuture(kp);
    }

    @Test
    public void toBeanText() {
        KivPerson kp = new KivPerson();
        kp.setCn("foo");
        String s = finder.toBeanText("foo");
        Assert.assertNotNull(s);
    }


}
