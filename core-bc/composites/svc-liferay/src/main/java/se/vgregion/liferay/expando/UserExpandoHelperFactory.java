package se.vgregion.liferay.expando;

/**
 * @author Patrik Bergström
 */
public class UserExpandoHelperFactory {
    public static UserExpandoHelper createDefaultUserExpandoHelper() {
        UserExpandoHelperImpl userExpandoHelper = new UserExpandoHelperImpl();
        userExpandoHelper.setExpandoUtil(ExpandoUtil.createDefaultExpandoUtil());

        return userExpandoHelper;
    }
}
