package se.vgregion.liferay.usergroup;

import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:29
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface UserGroupHelper {

    void addUser(UserGroup userGroup, User... user);
    void addUser(String userGroupName, User... user);

    void removeUser(UserGroup userGroup, User... user);
    void removeUser(String userGroupName, User... user);

    UserGroup findByName(String userGroupName, long companyId);

    boolean isMember(UserGroup userGroup, User user);

    void createIfNeeded(String userGroupName, long companyId);

    void rename(String newUserGroupName, UserGroup userGroup);

    void moveUsers(UserGroup fromGroup, UserGroup toGroup);

    void delete(UserGroup userGroup);

    void processInternalAccessOnly(User user);

    void processExternallySithsOnlyAccess(User user);
}
