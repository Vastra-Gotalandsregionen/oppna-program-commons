package se.vgregion.portal.cs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.portal.cs.domain.UserSiteCredential;
import se.vgregion.portal.cs.domain.persistence.UserSiteCredentialRepository;
import se.vgregion.portal.cs.util.AesCtrCryptoUtilImpl;
import se.vgregion.portal.cs.util.CryptoUtilImpl;

import javax.annotation.Resource;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Service class for migrating cipher texts to new ciphers and new keys. This implementation migrates between
 * ECB and CTR block mode for AES enryption.
 *
 * @see se.vgregion.portal.cs.util.CryptoUtil
 * @see javax.crypto.Cipher
 * @author Patrik Bergström
 */
public class MigrationServiceImpl implements MigrationService {

    private static final Logger LOGGER = Logger.getLogger(MigrationServiceImpl.class.getName());

    @Autowired
    private UserSiteCredentialRepository repository;

    @Resource(name = "aesCtrCryptoUtil")
    private AesCtrCryptoUtilImpl ctrCryptoUtil;
    @Resource(name = "cryptoUtil")
    private CryptoUtilImpl ecbCryptoUtil;

    private String newCvKeyPath = "newCv.key";

    /**
     * Migrate cipher texts from all {@link UserSiteCredential}s from ECB block mode to CTR block mode.
     */
    @Transactional
    public void migrateEcbToCtr() {
        Collection<UserSiteCredential> all = findAll();
        for (UserSiteCredential usc : all) {
            String decrypt = null;
            try {
                decrypt = ecbCryptoUtil.decrypt(usc.getSitePassword());
                String ctrEncrypt = ctrCryptoUtil.encrypt(decrypt);
                usc.setSitePassword(ctrEncrypt);
                merge(usc);
                LOGGER.info("CTR encrypted: " + ctrEncrypt + " - saved.");
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Migrate cipher texts from all {@link UserSiteCredential}s from CTR block mode to ECB block mode.
     */
    @Transactional
    public void migrateCtr2Ecb() {
        Collection<UserSiteCredential> all = findAll();
        for (UserSiteCredential usc : all) {
            String decrypt = null;
            try {
                decrypt = ctrCryptoUtil.decrypt(usc.getSitePassword());
                String ecbEncrypt = ecbCryptoUtil.encrypt(decrypt);
                usc.setSitePassword(ecbEncrypt);
                merge(usc);
                LOGGER.info("ECB encrypted: " + ecbEncrypt + " - saved.");
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e); //roll back
            }
        }
    }

    /**
     * Migrate cipher texts from all {@link UserSiteCredential}s from the old key to a new key. I.e. the new
     * cipher texts can only be decrypted with the key.
     *
     * @return the file containing the new key
     */
    @Transactional
    public File migrateAndUpdateKey() {
        File newKeyFile = new File(newCvKeyPath);

        if (!newKeyFile.exists()) {
            ctrCryptoUtil.createKeyFile(newKeyFile);
        }
        
        AesCtrCryptoUtilImpl aesCtrCryptoUtilNew = new AesCtrCryptoUtilImpl(newKeyFile);
        Collection<UserSiteCredential> all = findAll();
        for (UserSiteCredential usc : all) {
            String decrypt;
            try {
                decrypt = ctrCryptoUtil.decrypt(usc.getSitePassword());
                String ctrEncrypt = aesCtrCryptoUtilNew.encrypt(decrypt);
                usc.setSitePassword(ctrEncrypt);
                merge(usc);
                LOGGER.info("New CTR encrypted: " + ctrEncrypt + " - saved.");
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e); //roll back
            }
        }
        return newKeyFile;
    }

    /**
     * Undo the work made by se.vgregion.portal.cs.migration.service.MigrationService#migrateAndUpdateKey(). The
     * new key file must still exist on the same location for this method to work.
     */
    @Transactional
    public void undoMigrateAndUpdateKey() {
        File newKeyFile = new File(newCvKeyPath);
        AesCtrCryptoUtilImpl aesCtrCryptoUtilNew = new AesCtrCryptoUtilImpl(newKeyFile);
        Collection<UserSiteCredential> all = findAll();
        for (UserSiteCredential usc : all) {
            String decrypt = null;
            try {
                decrypt = aesCtrCryptoUtilNew.decrypt(usc.getSitePassword());
                String ctrEncrypt = ctrCryptoUtil.encrypt(decrypt);
                usc.setSitePassword(ctrEncrypt);
                merge(usc);
                LOGGER.info("Old CTR encrypted: " + ctrEncrypt + " - saved.");
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e); //roll back
            }
        }
    }

    /**
     * Merges a {@link UserSiteCredential}.
     *
     * @param credential the {@link UserSiteCredential} instance to merge
     */
    @Transactional
    public void merge(UserSiteCredential credential) {
        repository.merge(credential);
    }

    /**
     * Finds all {@link UserSiteCredential}s.
     *
     * @return all {@link UserSiteCredential}s found in underlying repository
     */
    public Collection<UserSiteCredential> findAll() {
        return repository.findAll();
    }

}
