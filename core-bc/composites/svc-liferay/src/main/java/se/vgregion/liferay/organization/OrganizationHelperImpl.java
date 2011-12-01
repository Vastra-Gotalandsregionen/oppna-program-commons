package se.vgregion.liferay.organization;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalService;
import com.liferay.portal.service.UserLocalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-29 15:32
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class OrganizationHelperImpl implements OrganizationHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationHelperImpl.class);

    @Autowired
    private OrganizationLocalService organizationLocalService;

    @Autowired
    private UserLocalService userLocalService;

    @Override
    public void addUser(List<String> organizationNames, User user) {

        for (String organizationName: organizationNames) {
            try {
                long organizationId = organizationLocalService.getOrganizationId(user.getCompanyId(),
                        organizationName);

                userLocalService.addOrganizationUsers(organizationId, new long[]{user.getUserId()});

            } catch (Exception e) {
                String msg = String.format("Failed to add user [%s] to organization [%s]",
                        user.getFullName(), organizationName);
                log(msg, e);
            }
        }
    }

    @Override
    public void removeUser(List<String> organizationNames, User user) {
        long[] users= {user.getUserId()};
        for (String organizationName: organizationNames) {
            try {
                long organizationId = organizationLocalService.getOrganizationId(user.getCompanyId(),
                        organizationName);


                userLocalService.unsetOrganizationUsers(organizationId, new long[]{user.getUserId()});

            } catch (Exception e) {
                String msg = String.format("Failed to add user [%s] to organization [%s]",
                        user.getFullName(), organizationName);
                log(msg, e);
            }
        }



    }

    @Override
    public Organization findByName(String organizationName, long companyId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isMember(Organization organization, User user) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
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
    public void createIfNeeded(String organizationName, long companyId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void log(String msg, Exception e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.warn(msg, e);
        } else {
            LOGGER.warn(msg);
        }
    }

}
