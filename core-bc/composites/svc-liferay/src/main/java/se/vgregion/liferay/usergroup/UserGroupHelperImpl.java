package se.vgregion.liferay.usergroup;

import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.UserGroupLocalService;
import com.liferay.portal.service.UserLocalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.vgregion.liferay.LiferayAutomation;
import se.vgregion.liferay.expando.UserExpandoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:51
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserGroupHelperImpl implements UserGroupHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupHelperImpl.class);
    private static final String POSTFIX_INTERNAL_ONLY = "_internal_only";
    private static final String POSTFIX_EXTERNALLY_SITHS_ONLY = "_externally_siths_only";

    @Autowired
    private LiferayAutomation liferayAutomation;

    @Autowired
    private UserGroupLocalService userGroupLocalService;

    @Autowired
    private UserLocalService userLocalService;

    @Autowired
    private UserExpandoHelper userExpandoHelper;

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

    @Override
    public void processInternalAccessOnly(User user) {
        Boolean internalAccess = null;
        try {
            internalAccess = userExpandoHelper.get("isInternalAccess", user);

            int cnt = userGroupLocalService.getUserGroupsCount();
            List<UserGroup> allUserGroups = userGroupLocalService.getUserGroups(0, cnt);
            List<UserGroup> allInternalUserGroups = internalOnlyGroups(allUserGroups);
            List<UserGroup> userGroups = user.getUserGroups();
            List<UserGroup> userInternalOnlyGroups = internalOnlyGroups(userGroups);

            List<String> userGroupNames = new ArrayList<String>();
            for (UserGroup userGroup : userGroups) {
                userGroupNames.add(userGroup.getName());
            }

            // Iterate over all internal groups. For each one, remove the user if access is not internal and he is
            // currently a member (or he is not a member of the corresponding internal group), or add the user if access
            // is internal and he is not yet a member (and he is a member of the corresponding internal group).
            for (UserGroup internalUserGroup: allInternalUserGroups) {
                String userGroupWithRole = internalOnlyCalculateUserGroupName(internalUserGroup);

                if (userGroupNames.contains(userGroupWithRole)) {
                    // User already in group. Should he be there?
                    if (!internalAccess || !userInternalOnlyGroups.contains(internalUserGroup)) {
                        // Nope
                        removeUser(userGroupWithRole, user);
                    }
                } else {
                    // User not member in group. Should he be there?
                    if (internalAccess && userInternalOnlyGroups.contains(internalUserGroup)) {
                        // Yup
                        addUser(userGroupWithRole, user);
                    }
                }
            }
        } catch (Exception e) {
            String msg = String.format("Failed to change UserGroup's according to internal_only access " +
                    "restrictions [%s] for [%s]", internalAccess, user.getScreenName());
            log(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public void processExternallySithsOnlyAccess(User user) {
        try {
            Boolean internalAccess = userExpandoHelper.get("isInternalAccess", user);
            Boolean externalSithsAccess = userExpandoHelper.get("isExternalSithsAccess", user);

            int cnt = userGroupLocalService.getUserGroupsCount();
            List<UserGroup> allUserGroups = userGroupLocalService.getUserGroups(0, cnt);
            List<UserGroup> allExternallySithsOnlyGroups = externallySithsOnlyGroups(allUserGroups);
            List<UserGroup> userGroups = user.getUserGroups();
            List<UserGroup> userExternallySithsOnlyGroups = externallySithsOnlyGroups(userGroups);

            List<String> userGroupNames = new ArrayList<String>();
            for (UserGroup userGroup : userGroups) {
                userGroupNames.add(userGroup.getName());
            }

            if (internalAccess || externalSithsAccess) {
                // The user should be member of the externallySithsOnly groups where the user is.
                for (UserGroup externallySithsOnlyGroup : allExternallySithsOnlyGroups) {
                    String userGroupWithRole = externallySithsOnlyCalculateUserGroupName(externallySithsOnlyGroup);

                    if (userGroupNames.contains(userGroupWithRole)) {
                        // Already a member
                    } else if (userExternallySithsOnlyGroups.contains(externallySithsOnlyGroup)) {
                        // Is member of the externallySithsOnly group but not a member of the corresponding
                        // original group, so add the user
                        addUser(userGroupWithRole, user);
                    }
                }
            } else {
                // To get here (!internalAccess && !externalSithsAccess) == true
                // Then the user should not be member of externallySithsOnly groups
                for (UserGroup externallySithsOnlyGroup : allExternallySithsOnlyGroups) {
                    String userGroupWithRole = internalOnlyCalculateUserGroupName(externallySithsOnlyGroup);

                    if (userGroupNames.contains(userGroupWithRole)) {
                        // Is member, remove.
                        removeUser(userGroupWithRole, user);
                    }
                }
            }
        } catch (Exception e) {
            log(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private List<UserGroup> internalOnlyGroups(List<UserGroup> allUserGroups) {
        List<UserGroup> result = new ArrayList<UserGroup>();
        for (UserGroup group : allUserGroups) {
            if (group.getName().endsWith(POSTFIX_INTERNAL_ONLY)) {
                result.add(group);
            }
        }
        return result;
    }

    private List<UserGroup> externallySithsOnlyGroups(List<UserGroup> allUserGroups) {
        List<UserGroup> result = new ArrayList<UserGroup>();
        for (UserGroup group : allUserGroups) {
            if (group.getName().endsWith(POSTFIX_EXTERNALLY_SITHS_ONLY)) {
                result.add(group);
            }
        }
        return result;
    }

    private String internalOnlyCalculateUserGroupName(UserGroup group) {
        String groupWithRightsName = group.getName().substring(0,
                group.getName().length() - POSTFIX_INTERNAL_ONLY.length());
        return groupWithRightsName;
    }

    private String externallySithsOnlyCalculateUserGroupName(UserGroup group) {
        String groupWithRightsName = group.getName().substring(0,
                group.getName().length() - POSTFIX_EXTERNALLY_SITHS_ONLY.length());
        return groupWithRightsName;
    }

    private boolean isInvalid(User... users) {
        if (users.length <= 0) return true;

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
