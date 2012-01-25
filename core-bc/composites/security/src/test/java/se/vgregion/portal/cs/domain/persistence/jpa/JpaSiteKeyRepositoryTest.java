package se.vgregion.portal.cs.domain.persistence.jpa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.persistence.SiteKeyRepository;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@ContextConfiguration("classpath:JpaRepositoryTest-context.xml")
public class JpaSiteKeyRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private SiteKeyRepository repo;

    @Before
    public void setup() {
        executeSqlScript("classpath:dbsetup/test-data.sql", false);
    }

    @Test
    public void testFindBySiteKey() {
        SiteKey siteKey = repo.findBySiteKey("test-key");

        assertEquals((Long) 1l, siteKey.getId());
        assertEquals("test-key", siteKey.getSiteKey());
        assertEquals("Test key 1", siteKey.getTitle());
        assertEquals("description", siteKey.getDescription());
        assertEquals(true, siteKey.getScreenNameOnly());
        assertEquals(false, siteKey.getSuggestScreenName());
        assertEquals(true, siteKey.getActive());
    }

    @Test
    public void testUpdateSiteKey() {
        SiteKey siteKey = repo.findBySiteKey("test-key");

        assertEquals("Test key 1", siteKey.getTitle());

        siteKey.setTitle("Test key updated");
        repo.save(siteKey);

        repo.flush();

        String result = simpleJdbcTemplate.queryForObject(
                "select title from vgr_site_key where site_key = 'test-key'",
                String.class);
        assertEquals("Test key updated", result);
    }

    @Test
    public void testInvalidSiteKey() {
        SiteKey siteKey = repo.findBySiteKey("invalid-key");
        assertNull(siteKey);
    }

    @Test
    public void testCreateSiteKey() {
        SiteKey siteKey = new SiteKey("new-site-key", "Test key created", "descr", true, false, true);

        repo.save(siteKey);
        repo.flush();

        String result = simpleJdbcTemplate.queryForObject(
                "select title from vgr_site_key where site_key = 'new-site-key'",
                String.class);
        assertEquals("Test key created", result);

        SiteKey newSiteKey = repo.findBySiteKey("new-site-key");
        assertNotNull(newSiteKey.getId());
    }
}
