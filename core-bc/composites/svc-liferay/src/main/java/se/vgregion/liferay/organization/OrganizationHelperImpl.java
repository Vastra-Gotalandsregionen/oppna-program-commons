package se.vgregion.liferay.organization;

import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-29 15:32
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class OrganizationHelperImpl implements OrganizationHelper {

    @Autowired
    private OrganizationLocalService organizationLocalService;

    @Override
    public void addUser(Organization organization, User... user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addUser(String organizationName, User... user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeUser(Organization organization, User... user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeUser(String organizationName, User... user) {
        //To change body of implemented methods use File | Settings | File Templates.
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
    public void createIfNeeded(String organizationName, long companyId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
