package se.vgregion.portal.cs.domain.persistence;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.cs.domain.UserSiteCredential;

import java.util.List;

public interface UserSiteCredentialRepository extends Repository<UserSiteCredential, Long> {

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

    List<UserSiteCredential> getAllSiteCredentials(String uid);

    /**
     * Store a credental. Password will be encrypted before storage.
     * 
     * @param siteCredential
     *            - credental to be stored
     */
    void save(UserSiteCredential siteCredential);

}
