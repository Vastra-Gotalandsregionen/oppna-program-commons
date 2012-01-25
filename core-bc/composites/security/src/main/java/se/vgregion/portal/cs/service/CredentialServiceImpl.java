package se.vgregion.portal.cs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.cs.domain.SiteKey;
import se.vgregion.portal.cs.domain.persistence.SiteKeyRepository;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.domain.persistence.UserSiteCredentialRepository;

import java.util.Collection;

/**
 * Service interface for Credential Store.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Service
public class CredentialServiceImpl implements CredentialService {

    @Autowired
    private UserSiteCredentialRepository userSiteCredentialRepository;

    @Autowired
    private SiteKeyRepository siteKeyRepository;

    @Override
    public UserSiteCredential getUserSiteCredential(String uid, String siteKey) {
        return userSiteCredentialRepository.getUserSiteCredential(uid, siteKey);
    }

    @Override
    public UserSiteCredential getUserSiteCredential(Long siteCredentialId) {
        return userSiteCredentialRepository.find(siteCredentialId);
    }

    @Override
    public Collection<UserSiteCredential> getAllSiteCredentials(String uid) {
        return userSiteCredentialRepository.getAllSiteCredentials(uid);
    }

    @Override
    public Collection<SiteKey> getAllSiteKeys() {
        return siteKeyRepository.findAll();
    }

    @Override
    public SiteKey getSiteKey(Long siteKeyId) {
        return siteKeyRepository.find(siteKeyId);
    }

    @Override
    public SiteKey getSiteKey(String siteKey) {
        return siteKeyRepository.findBySiteKey(siteKey);
    }

    @Transactional
    @Override
    public void save(UserSiteCredential siteCredential) {
        userSiteCredentialRepository.save(siteCredential);
    }

    @Transactional
    @Override
    public void save(SiteKey siteKey) {
        siteKeyRepository.save(siteKey);
    }

    @Transactional
    @Override
    public void removeUserSiteCredential(Long siteCredentialId) {
        userSiteCredentialRepository.remove(siteCredentialId);
    }

    @Transactional
    @Override
    public void removeSiteKey(Long siteKeyId) {
        siteKeyRepository.remove(siteKeyId);
    }

}
