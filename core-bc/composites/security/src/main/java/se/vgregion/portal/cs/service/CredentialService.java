package se.vgregion.portal.cs.service;

import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.UserSiteCredential;

import java.util.Collection;

/**
 * Service interface for Credential Store.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface CredentialService {

    /**
     * Retrive user credentials. If no credentals are stored, null will be returned.
     * 
     * @param uid
     *            - user identifier.
     * @param siteKey
     *            - site credental identifier.
     * @return credentials
     */
    UserSiteCredential getUserSiteCredential(String uid, String siteKey);

    UserSiteCredential getUserSiteCredential(Long siteCredentialId);

    Collection<UserSiteCredential> getAllSiteCredentials(String uid);

    Collection<SiteKey> getAllSiteKeys();

    SiteKey getSiteKey(Long siteKeyId);

    SiteKey getSiteKey(String siteKey);

    /**
     * Store a credental. Password will be encrypted before storage.
     * 
     * @param siteCredential
     *            - credental to be stored
     */
    void save(UserSiteCredential siteCredential);

    void save(SiteKey siteKey);

    void removeUserSiteCredential(Long siteCredentialId);

    void removeSiteKey(Long siteKeyId);
}
