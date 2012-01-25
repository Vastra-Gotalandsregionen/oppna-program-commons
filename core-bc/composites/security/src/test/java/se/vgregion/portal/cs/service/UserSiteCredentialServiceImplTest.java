/**
 * 
 */
package se.vgregion.portal.cs.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.domain.persistence.SiteKeyRepository;
import se.vgregion.portal.cs.domain.persistence.UserSiteCredentialRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author anders.bergkvist@omegapoint.se
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserSiteCredentialServiceImplTest {

    CredentialServiceImpl credentialService;

    @Mock
    UserSiteCredentialRepository userSiteCredentialRepository;

    @Mock
    SiteKeyRepository siteKeyRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        credentialService = new CredentialServiceImpl();

        ReflectionTestUtils.setField(credentialService, "userSiteCredentialRepository",
                userSiteCredentialRepository);
        ReflectionTestUtils.setField(credentialService, "siteKeyRepository",
                siteKeyRepository);
    }

    @Test
    public void testGetUserSiteCredential() {
        String uid = "screenName";
        String siteKey = "appName";
        UserSiteCredential expected = new UserSiteCredential();
        expected.setUid(uid);
        expected.setSiteKey(siteKey);
        expected.setId(Long.valueOf(1));

        when(userSiteCredentialRepository.getUserSiteCredential(anyString(), anyString())).thenReturn(expected);

        UserSiteCredential returned = credentialService.getUserSiteCredential(uid, siteKey);
        assertEquals(uid, returned.getUid());
        assertEquals(siteKey, returned.getSiteKey());
        assertEquals(Long.valueOf(1), returned.getId());
    }

    @Test
    public void testGetAllSiteCredentials() {
        when(userSiteCredentialRepository.getAllSiteCredentials("uid"))
                .thenReturn(Collections.<UserSiteCredential>emptyList());

        Collection<UserSiteCredential> result = credentialService.getAllSiteCredentials("uid");

        assertEquals(0, result.size());
    }

    @Test
    public void testGetUserSiteCredentialById() {
        when(userSiteCredentialRepository.find(0L)).thenReturn(new UserSiteCredential("uid", "siteKey"));

        UserSiteCredential result = credentialService.getUserSiteCredential(0L);

        assertEquals("uid", result.getUid());
        assertEquals("siteKey", result.getSiteKey());
    }

    @Test
    public void testAddUserSiteCredential() {
        String uid = "screenName";
        String siteKey = "appName";

        UserSiteCredential create = new UserSiteCredential();
        create.setUid(uid);
        create.setSiteKey(siteKey);
        create.setId(Long.valueOf(1));

        credentialService.save(create);

        verify(userSiteCredentialRepository).save(any(UserSiteCredential.class));
    }

    @Test
    public void testGetAllSiteKeys() {
        when(siteKeyRepository.findAll()).thenReturn(Arrays.asList(new SiteKey()));

        Collection<SiteKey> result = credentialService.getAllSiteKeys();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetSiteKeyById() {
        SiteKey siteKey = new SiteKey();
        when(siteKeyRepository.find(0L)).thenReturn(siteKey);

        SiteKey result = credentialService.getSiteKey(0L);

        assertSame(siteKey, result);
    }

    @Test
    public void testGetSiteKeyBySiteKey() {
        SiteKey siteKey = new SiteKey();
        when(siteKeyRepository.findBySiteKey("siteKey")).thenReturn(siteKey);

        SiteKey result = credentialService.getSiteKey("siteKey");

        assertSame(siteKey, result);
    }

    @Test
    public void testSaveUserSiteCredential() {
        UserSiteCredential cred = new UserSiteCredential();

        credentialService.save(cred);

        verify(userSiteCredentialRepository).save(cred);
    }

    @Test
    public void testSaveSiteKey() {
        SiteKey siteKey = new SiteKey();

        credentialService.save(siteKey);

        verify(siteKeyRepository).save(siteKey);
    }

    @Test
    public void testRemoveUserSiteCredential() {
        credentialService.removeUserSiteCredential(0L);

        verify(userSiteCredentialRepository).remove(0L);
    }

    @Test
    public void testRemoveSiteKey() {
        credentialService.removeSiteKey(0L);

        verify(siteKeyRepository).remove(0L);
    }
}
