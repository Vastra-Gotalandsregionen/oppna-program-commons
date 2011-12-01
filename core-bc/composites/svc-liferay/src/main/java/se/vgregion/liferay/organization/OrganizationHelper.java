package se.vgregion.liferay.organization;

import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-29 15:32
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface OrganizationHelper {
    void addUser(Organization organization, User... user);
    void addUser(String organizationNames, User... user);

    void removeUser(Organization organization, User... user);
    void removeUser(String organizationNames, User... user);

    Organization findByName(String organizationName, long companyId);

    boolean isMember(Organization organization, User user);
    boolean isMember(String organizationName, User user);

    void createIfNeeded(String organizationName, Organization parent, long companyId);
    void createIfNeeded(String organizationName, long companyId);

}
