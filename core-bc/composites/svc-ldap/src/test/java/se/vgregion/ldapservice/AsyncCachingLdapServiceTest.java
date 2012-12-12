package se.vgregion.ldapservice;

import net.sf.ehcache.Ehcache;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.StopWatch;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Bergström
 */
public class AsyncCachingLdapServiceTest {

    @Test
    public void testGetLdapUserByUidAsyncCapability() throws Exception {
        SlowMockLdapService ldapService = new SlowMockLdapService(2000);

        AsyncCachingLdapServiceWrapper asyncCachingLdapService = new AsyncCachingLdapServiceWrapper(ldapService, 5);

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
        assertNotNull(ldapUser.getDn());
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

    // The AsyncCachingLdapServiceWrapper should remove objects that are null from cache since they may be null because
    // of some error and we don't want that to be cached.
    @Test
    public void testClearCacheFromNullObjects() throws InterruptedException {
        ReturnNullMockLdapService ldapService = new ReturnNullMockLdapService();

        AsyncCachingLdapServiceWrapper asyncCachingLdapService = new AsyncCachingLdapServiceWrapper(ldapService, 50000);

        LdapUser ldapUser = asyncCachingLdapService.getLdapUserByUid("a specific id");

        Ehcache cache = asyncCachingLdapService.getCache();

        assertEquals(1, cache.getStatistics().getObjectCount());

        Thread.sleep(6000);

        assertEquals(0, cache.getStatistics().getObjectCount());
    }

    @Test
    public void testSerializeAsyncLdapUserWrapper() throws IOException, ClassNotFoundException {

        // Write output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        LdapUser ldapUser = new SimpleLdapUser("someDn");
        LdapService ldapService = Mockito.mock(LdapService.class);
        AsyncCachingLdapServiceWrapper serviceWrapper = new AsyncCachingLdapServiceWrapper(ldapService);
        AsyncCachingLdapServiceWrapper.AsyncLdapUserWrapper ldapUserWrapper = new AsyncCachingLdapServiceWrapper
                .AsyncLdapUserWrapper(new AsyncResult<LdapUser>(ldapUser), 1234);
        oos.writeObject(ldapUserWrapper);

        // Read input
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        LdapUser ldapUserDeserialized = (LdapUser) ois.readObject();
        String dn = ldapUserDeserialized.getDn();

        // Verify
        assertEquals("someDn", dn);
    }
}
