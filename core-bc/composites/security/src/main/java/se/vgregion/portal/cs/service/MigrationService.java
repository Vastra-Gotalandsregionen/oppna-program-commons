package se.vgregion.portal.cs.service;

import se.vgregion.portal.cs.domain.UserSiteCredential;

import java.io.File;
import java.util.Collection;

/**
 * Service class for migrating cipher texts to new ciphers and new keys.
 *
 * @see se.vgregion.portal.cs.util.CryptoUtil
 * @see javax.crypto.Cipher
 * @author Patrik Bergström
 */
public interface MigrationService {

    /**
     * Migrate cipher texts from all {@link se.vgregion.portal.cs.domain.UserSiteCredential}s from ECB block mode
     * to CTR block mode.
     */
    void migrateEcbToCtr();

    /**
     * Migrate cipher texts from all {@link se.vgregion.portal.cs.domain.UserSiteCredential}s from CTR block mode
     * to ECB block mode.
     */
    void migrateCtr2Ecb();

    /**
     * Migrate cipher texts from all {@link se.vgregion.portal.cs.domain.UserSiteCredential}s from the old key to
     * a new key. I.e. the new
     * cipher texts can only be decrypted with the key.
     *
     * @return the file containing the new key
     */
    File migrateAndUpdateKey();

    /**
     * Undo the work made by se.vgregion.portal.cs.migration.service.MigrationService#migrateAndUpdateKey(). The
     * new key file must still exist on the same location for this method to work.
     */
    void undoMigrateAndUpdateKey();

    /**
     * Merges a {@link se.vgregion.portal.cs.domain.UserSiteCredential}.
     *
     * @param credential the {@link se.vgregion.portal.cs.domain.UserSiteCredential} instance to merge
     */
    void merge(UserSiteCredential credential);

    /**
     * Finds all {@link se.vgregion.portal.cs.domain.UserSiteCredential}s.
     *
     * @return all {@link se.vgregion.portal.cs.domain.UserSiteCredential}s found in underlying repository
     */
    Collection<UserSiteCredential> findAll();

}
