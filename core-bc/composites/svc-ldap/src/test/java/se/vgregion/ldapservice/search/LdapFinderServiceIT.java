package se.vgregion.ldapservice.search;

import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: Claes Lundahl
 * Date: 2013-11-26
 */
public class LdapFinderServiceIT {

    private String userId = "someRealUserId";

    @Test
    public void findFromWebLdap() {
        WebLdapPerson person = new WebLdapPerson();
        person.setUid(userId);
        List<?> result = find("classpath*:webb-ldap.xml", person);
        //System.out.println(toBeanText("classpath*:ad-ldap.xml"));
        System.out.println(result);
    }

    @Test
    public void findFromAd() {
        AdPerson person = new AdPerson();
        person.setCn(userId);
        List<?> result = find("classpath*:ad-ldap.xml", person);
        //System.out.println(toBeanText("classpath*:ad-ldap.xml"));
        System.out.println(result);
    }

    @Test
    public void findFromKiv() {
        KivPerson person = new KivPerson();
        person.setCn(userId);
        List<?> result = find("classpath*:kiv-ldap.xml", person);
        //System.out.println(toBeanText("classpath*:kiv-ldap.xml"));
        System.out.println(result);
    }

    @Test
    public void asyncFind() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        KivPerson kivPerson = new KivPerson();
        kivPerson.setCn(userId);
        Future<List<Object>> kivs = findAsync("classpath*:kiv-ldap.xml", kivPerson);

        WebLdapPerson webbPerson = new WebLdapPerson();
        webbPerson.setUid(userId);
        Future<List<Object>> webbs = findAsync("classpath*:webb-ldap.xml", webbPerson);

        AdPerson adPerson = new AdPerson();
        adPerson.setCn(userId);
        Future<List<Object>> ads = findAsync("classpath*:ad-ldap.xml", adPerson);

        System.out.println(userId + " in ad " + ads.get());
        System.out.println(userId + " in kiv " + kivs.get());
        System.out.println(userId + " in webb-ldap " + webbs.get());

        System.out.println("It took " + (System.currentTimeMillis() - startTime) + " ms.");
    }


    public String toBeanText(String confFile) {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load(confFile);
        ctx.refresh();

        LdapFinderService finder = ctx.getBean("ldapService", LdapFinderService.class);
        return finder.toBeanText(userId);
    }

    public List<?> find(String confFile, Object template) {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load(confFile);
        ctx.refresh();

        LdapFinderService finder = ctx.getBean("ldapService", LdapFinderService.class);
        List<?> result = finder.find(template);
        return result;
    }

    public Future<List<Object>> findAsync(String confFile, Object template) {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load(confFile);
        ctx.refresh();

        LdapFinderService finder = ctx.getBean("ldapService", LdapFinderService.class);
        Future<List<Object>> result = finder.findFuture(template);
        return result;
    }

}
