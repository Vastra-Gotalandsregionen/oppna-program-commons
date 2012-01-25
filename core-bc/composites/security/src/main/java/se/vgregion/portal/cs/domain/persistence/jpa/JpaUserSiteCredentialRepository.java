package se.vgregion.portal.cs.domain.persistence.jpa;

import org.springframework.stereotype.Repository;

import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.domain.persistence.UserSiteCredentialRepository;
import se.vgregion.portal.cs.util.CryptoUtil;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Jpa implementation of User-SiteCredential Repository.
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Repository
public class JpaUserSiteCredentialRepository extends DefaultJpaRepository<UserSiteCredential, Long> implements
        UserSiteCredentialRepository {

    @Resource(name = "cryptoUtil")
    private CryptoUtil cryptoUtil;

    public void setCryptoUtil(CryptoUtil cryptoUtil) {
        this.cryptoUtil = cryptoUtil;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
        String queryString = "SELECT s FROM UserSiteCredential s WHERE s.uid = :uid and s.siteKey = :siteKey";
        Query query = entityManager.createQuery(queryString).setParameter("uid", uid)
                .setParameter("siteKey", siteKey);

        @SuppressWarnings("unchecked")
        List<UserSiteCredential> results = query.getResultList();
        if (results.size() == 0) {
            return null;
        }
        if (results.size() > 1) {
            String message = "Argument uid=" + uid + " and siteKey=" + siteKey
                    + " did not match a unique post of the type " + UserSiteCredential.class.getSimpleName();
            throw new IllegalArgumentException(message);
        }

        UserSiteCredential result = results.get(0);
        try {
            decryptSitePwd(result);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to decrypt password", e);
        }
        return result;
    }

    @Override
    public List<UserSiteCredential> getAllSiteCredentials(String uid) {
        String queryString = "SELECT s FROM UserSiteCredential s WHERE s.uid = :uid";
        Query query = entityManager.createQuery(queryString).setParameter("uid", uid);

        List<UserSiteCredential> result = query.getResultList();
        for (UserSiteCredential credential : result) {
            try {
                decryptSitePwd(credential);
            } catch (GeneralSecurityException e) {
                credential.setValid(false);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(UserSiteCredential siteCredential) {
        encryptSitePwd(siteCredential);
        if (siteCredential.getId() == null) {
            entityManager.persist(siteCredential);
        } else {
            entityManager.merge(siteCredential);
        }
    }

    private void decryptSitePwd(UserSiteCredential creds) throws GeneralSecurityException {
        String encryptedPwd = creds.getSitePassword();
        String clearPwd = null;
        clearPwd = cryptoUtil.decrypt(encryptedPwd);
        creds.setSitePassword(clearPwd);
        creds.setValid(true);
    }

    private void encryptSitePwd(UserSiteCredential siteCredential) {
        try {
            String clearPwd = siteCredential.getSitePassword();
            String encryptedPwd = cryptoUtil.encrypt(clearPwd);
            siteCredential.setSitePassword(encryptedPwd);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

}
