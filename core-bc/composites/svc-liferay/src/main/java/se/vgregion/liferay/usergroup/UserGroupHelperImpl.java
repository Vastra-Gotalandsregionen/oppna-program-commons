package se.vgregion.liferay.usergroup;

import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.UserGroupLocalService;
import com.liferay.portal.service.UserLocalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.vgregion.liferay.LiferayAutomation;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:51
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserGroupHelperImpl implements UserGroupHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupHelperImpl.class);

    @Autowired
    private LiferayAutomation liferayAutomation;

    @Autowired
    private UserGroupLocalService userGroupLocalService;

    @Autowired
    private UserLocalService userLocalService;

    @Override
    public void addUser(UserGroup userGroup, User... users) {
        if (isInvalid(users)) return;

        if (userGroup != null) {
            try {
                userLocalService.addUserGroupUsers(userGroup.getUserGroupId(), toIdArray(users));
            } catch (Exception e) {
                String msg = String.format("Failed to add users [%s] to UserGroup [%s]",
                        toScreenNames(users), userGroup.getName());
                log(msg, e);
            }
        }
    }

    @Override
    public void addUser(String userGroupName, User... users) {
        if (isInvalid(users)) return;

        try {
            UserGroup userGroup = findByName(userGroupName, users[0].getCompanyId());
            if (userGroup == null) {
                // create and try again
                createIfNeeded(userGroupName, users[0].getCompanyId());
                addUser(userGroupName, users);
            } else {
                addUser(userGroup, users);
            }
        } catch (Exception e) {
            String msg = String.format("Failed to add users [%s] to UserGroup [%s]",
                    toScreenNames(users), userGroupName);
            log(msg, e);
        }
    }

    @Override
    public void removeUser(UserGroup userGroup, User... users) {
        if (isInvalid(users)) return;

        if (userGroup != null) {
            try {
                userLocalService.unsetUserGroupUsers(userGroup.getUserGroupId(), toIdArray(users));
            } catch (Exception e) {
                String msg = String.format("Failed to remove users [%s] from UserGroup [%s]",
                        toScreenNames(users), userGroup.getName());
                log(msg, e);
            }
        }
    }

    @Override
    public void removeUser(String userGroupName, User... users) {
        if (isInvalid(users)) return;

        UserGroup userGroup = findByName(userGroupName, users[0].getCompanyId());
        removeUser(userGroup, users);
    }

    @Override
    public UserGroup findByName(String userGroupName, long companyId) {
        try {
            return userGroupLocalService.getUserGroup(companyId, userGroupName);
        } catch (Exception e) {
            String msg = String.format("Unable to find UserGroup [%s] for companyId [%s]",
                    userGroupName, companyId);
            log(msg, e);
        }
        return null;
    }

    @Override
    public boolean isMember(UserGroup userGroup, User user) {
        try {
            return userLocalService.hasUserGroupUser(userGroup.getUserGroupId(), user.getUserId());
        } catch (Exception e) {
            String msg = String.format("Failed to lookup if user belongs to group [%s, %s]",
                    user.getScreenName(), userGroup.getName());
            log(msg, e);
        }
        return false;
    }

    @Override
    public void createIfNeeded(String userGroupName, long companyId) {
        try {
            if (findByName(userGroupName, companyId) != null) return;

            User systemUser = liferayAutomation.lookupSysadmin(companyId);

            String description = liferayAutomation.autoCreateDescription();
            userGroupLocalService.addUserGroup(systemUser.getUserId(), systemUser.getCompanyId(), userGroupName,
                    description);
        } catch (Exception e) {
            String msg = String.format("Failed to create UserGroup [%s]", userGroupName);
            log(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void rename(String newUserGroupName, UserGroup userGroup) {
        try {
            String description = liferayAutomation.autoRenameDescription(userGroup.getName(), newUserGroupName);
            userGroup.setName(newUserGroupName);
            userGroup.setDescription(userGroup.getDescription() + description);
            userGroupLocalService.updateUserGroup(userGroup);
        } catch (Exception e) {
            String msg = String.format("Failed to rename UserGroup from [%s] to [%s]", userGroup.getName(),
                    newUserGroupName);
            log(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void moveUsers(UserGroup fromGroup, UserGroup toGroup) {
        try {
            List<User> users = userLocalService.getUserGroupUsers(fromGroup.getUserGroupId());
            long[] userIds = toIdArray(users.toArray(new User[]{}));

            userLocalService.addUserGroupUsers(toGroup.getUserGroupId(), userIds);

            userLocalService.unsetUserGroupUsers(fromGroup.getUserGroupId(), userIds);
        } catch (Exception e) {
            String msg = String.format("Failed to users from [%s] to [%s]", fromGroup.getName(),
                    toGroup.getName());
            log(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void delete(UserGroup userGroup) {
        try {
            List<User> users = userLocalService.getUserGroupUsers(userGroup.getUserGroupId());
            long[] userIds = toIdArray(users.toArray(new User[]{}));

            userLocalService.unsetUserGroupUsers(userGroup.getUserGroupId(), userIds);

            userGroupLocalService.deleteUserGroup(userGroup);
        } catch (Exception e) {
            String msg = String.format("Failed to delete UserGroup [%s]", userGroup.getName());
            log(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private boolean isInvalid(User... users) {
        if (users.length <= 0) return true;

        if (users.length > 1) {
            long companyId = users[0].getCompanyId();
            for (User user : users) {
                if (user.getCompanyId() != companyId) {
                    LOGGER.error("User are from different Liferay instances");
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
            if (sb.length() > 0) sb.append(", ");
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
