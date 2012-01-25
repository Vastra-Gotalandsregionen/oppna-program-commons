package se.vgregion.portal.cs.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * SiteKey domain object.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Entity
@Table(name = "vgr_site_key")
public class SiteKey extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = -7040534908217689248L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 50, name = "site_key")
    private String siteKey;

    @Column(nullable = false, unique = true, length = 50)
    private String title;

    @Column(length = 1024)
    private String description;

    private Boolean suggestScreenName;

    private Boolean screenNameOnly;

    private Boolean active;

    /**
     * Empty default constructor.
     */
    public SiteKey() {
    }

    public SiteKey(String siteKey, String title, String description, Boolean suggestScreenName, Boolean screenNameOnly, Boolean active) {
        this.title = title;
        this.suggestScreenName = suggestScreenName;
        this.siteKey = siteKey;
        this.screenNameOnly = screenNameOnly;
        this.description = description;
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getScreenNameOnly() {
        return screenNameOnly;
    }

    public void setScreenNameOnly(Boolean screenNameOnly) {
        this.screenNameOnly = screenNameOnly;
    }

    public Boolean getSuggestScreenName() {
        return suggestScreenName;
    }

    public void setSuggestScreenName(Boolean suggestScreenName) {
        this.suggestScreenName = suggestScreenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("siteKey", siteKey)
                .append("title", title).append("description", description).append("active", active).toString();
    }
}
