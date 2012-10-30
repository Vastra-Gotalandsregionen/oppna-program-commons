package se.vgregion.ldapservice;

import org.junit.Test;
import org.springframework.util.StopWatch;

import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Bergström
 */
public class AsyncCachingLdapServiceTest {

    @Test
    public void testGetLdapUserByUidAsyncCapability() throws Exception {
        SlowMockLdapService ldapService = new SlowMockLdapService(2000);

        AsyncCachingLdapServiceWrapper asyncCachingLdapService = new AsyncCachingLdapServiceWrapper(ldapService, 5000);

        LdapUser ldapUser = asyncCachingLdapService.getLdapUserByUid("doesn't matter");

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        String mailServer = ldapUser.getAttributeValue("mailServer");
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        // It should be roughly 2 seconds.
        assertTrue(totalTimeMillis > 1500 && totalTimeMillis < 2500);

        ldapUser = asyncCachingLdapService.getLdapUserByUid("just another user");

        // If we wait now the user attributes should be ready.
        Thread.sleep(2000);

        stopWatch = new StopWatch();
        stopWatch.start();
        ldapUser.getAttributeValue("mailServer");
        stopWatch.stop();

        assertTrue(stopWatch.getTotalTimeMillis() < 100);
    }

    @Test
    public void testGetLdapUserByUidCacheCapability() throws Exception {
        SlowMockLdapService ldapService = new SlowMockLdapService(500);

        AsyncCachingLdapServiceWrapper asyncCachingLdapService = new AsyncCachingLdapServiceWrapper(ldapService, 5000);

        LdapUser ldapUser = asyncCachingLdapService.getLdapUserByUid("a specific id");

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        String mailServer = ldapUser.getAttributeValue("mailServer");
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        // It should be roughly half a second.
        assertTrue(totalTimeMillis > 490 && totalTimeMillis < 800);

        ldapUser = asyncCachingLdapService.getLdapUserByUid("a specific id");

        // The result should now be cached and be fast to fetch.
        stopWatch = new StopWatch();
        stopWatch.start();
        mailServer = ldapUser.getAttributeValue("mailServer");
        stopWatch.stop();

        assertTrue(stopWatch.getTotalTimeMillis() < 100);
    }
}
