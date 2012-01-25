package se.vgregion.portal.cs.domain.persistence;

import se.vgregion.dao.domain.patterns.repository.Repository;
import se.vgregion.portal.cs.domain.SiteKey;

/**
 * SiteKey Repository interface.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface SiteKeyRepository extends Repository<SiteKey, Long> {

    void save(SiteKey siteKey);

    SiteKey findBySiteKey(String siteKey);
}
