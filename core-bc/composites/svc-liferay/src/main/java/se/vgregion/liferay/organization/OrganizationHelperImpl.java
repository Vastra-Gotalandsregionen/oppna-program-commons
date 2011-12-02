package se.vgregion.liferay.organization;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.vgregion.liferay.LiferayAutomation;

import com.liferay.portal.model.ListTypeConstants;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.OrganizationConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalService;
import com.liferay.portal.service.UserLocalService;

/**
 * Created by IntelliJ IDEA. Created: 2011-11-29 15:32
 * 
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class OrganizationHelperImpl implements OrganizationHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationHelperImpl.class);

    @Autowired
    private OrganizationLocalService organizationLocalService;

    @Autowired
    private LiferayAutomation liferayAutomation;

    @Autowired
    private UserLocalService userLocalService;

    @Override
    public void addUser(Organization organization, User... users) {
        if (isInvalid(users)) {
            return;
        }
        try {
            userLocalService.addOrganizationUsers(organization.getOrganizationId(), toIdArray(users));
        } catch (Exception e) {
            String msg = String.format("Failed to add users [%s] to Organization [%s]", toScreenNames(users),
                    organization.getName());
            log(msg, e);
        }
    }

    @Override
    public void addUser(String organizationName, User... users) {
        if (isInvalid(users)) {
            return;
        }

        try {
            Organization organization = findByName(organizationName, users[0].getCompanyId());
            if (organization == null) {
                // create and try again
                createIfNeeded(organizationName, users[0].getCompanyId());
                addUser(organizationName, users);
            } else {
                addUser(organization, users);
            }

        } catch (Exception e) {
            String msg = String.format("Failed to add users [%s] to Organization [%s]", toScreenNames(users),
                    organizationName);
            log(msg, e);
        }
    }

    @Override
    public void removeUser(Organization organization, User... users) {
        if (isInvalid(users)) {
            return;
        }

        if (organization != null) {
            try {
                userLocalService.unsetOrganizationUsers(organization.getOrganizationId(), toIdArray(users));
            } catch (Exception e) {
                String msg = String.format("Failed to remove users [%s] from Organization [%s]",
                        toScreenNames(users), organization.getName());
                log(msg, e);
            }
        }
    }

    @Override
    public void removeUser(String organizationName, User... users) {
        if (isInvalid(users)) {
            return;
        }

        try {
            Organization organization = findByName(organizationName, users[0].getCompanyId());

            removeUser(organization, users);
        } catch (Exception e) {
            String msg = String.format("Failed to add user [%s] to organization [%s]", toScreenNames(users),
                    organizationName);
            log(msg, e);
        }
    }

    @Override
    public Organization findByName(String organizationName, long companyId) {
        try {
            return organizationLocalService.getOrganization(companyId, organizationName);
        } catch (Exception e) {
            String msg = String.format("Failed to find organization [%s] in company [%s]", organizationName,
                    companyId);
            log(msg, e);
        }
        return null;
    }

    @Override
    public boolean isMember(Organization organization, User user) {
        try {
            return user.getOrganizations().contains(organization);
        } catch (Exception e) {
            String msg = String.format("Failed to lookup if user [%s] is member in organization [%s]",
                    user.getScreenName(), organization.getName());
            log(msg, e);
            throw new RuntimeException(msg, e);

        }
    }

    @Override
    public boolean isMember(String organizationName, User user) {
        try {
            List<Organization> userOrganizations = user.getOrganizations();
            for (Organization organization : userOrganizations) {
                if (organization.getName().equals(organizationName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            String msg = String.format("Failed to lookup if user [%s] is member in organization [%s]",
                    user.getScreenName(), organizationName);
            log(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Organization createIfNeeded(String organizationName, Organization parent, long companyId) {
        try {
            Organization organization = findByName(organizationName, companyId);

            if (organization != null) {
                return organization;
            }

            User systemUser = liferayAutomation.lookupSysadmin(companyId);

            Long parentId = (parent != null) ? parent.getOrganizationId() : 0L;
            String description = liferayAutomation.autoCreateDescription();
            organization = organizationLocalService.addOrganization(systemUser.getUserId(), parentId,
                    organizationName, OrganizationConstants.TYPE_REGULAR_ORGANIZATION, true, 0, 0,
                    ListTypeConstants.ORGANIZATION_STATUS_DEFAULT, description, null);

            return organization;
        } catch (Exception e) {
            String msg = String.format("Failed to create UserGroup [%s]", organizationName);
            log(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public Organization createIfNeeded(String organizationName, long companyId) {
        return createIfNeeded(organizationName, null, companyId);
    }

    private boolean isInvalid(User... users) {
        if (users.length <= 0) {
            return true;
        }

        if (users.length > 1) {
            long companyId = users[0].getCompanyId();
            for (User user : users) {
                if (user.getCompanyId() != companyId) {
                    LOGGER.error("Users are from different Liferay instances (they have different companyId)");
                    return true;
                }
            }
        }

        return false;
    }

    private long[] toIdArray(User... users) {
        long[] userIds = new long[users.length];
        for (int i = 0; i < users.length; i++) {
            userIds[i] = users[i].getUserId();
        }
        return userIds;
    }

    private String toScreenNames(User... users) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < users.length; i++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(users[i].getScreenName());
        }
        return sb.toString();
    }

    private void log(String msg, Exception e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.warn(msg, e);
        } else {
            LOGGER.warn(msg);
        }
    }

}
