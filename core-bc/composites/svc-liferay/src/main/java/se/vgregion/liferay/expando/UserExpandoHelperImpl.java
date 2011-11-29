package se.vgregion.liferay.expando;

import com.liferay.portal.model.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 15:14
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserExpandoHelperImpl implements UserExpandoHelper {

    @Autowired
    private ExpandoUtil expandoUtil;

    @Override
    public <T> void set(String columnName, T value, long companyId, long userId) {
        expandoUtil.setExpando(USER_CLASSNAME, columnName, value, companyId, userId, ExpandoUtil.Mode.AUTO_CREATE);
    }

    @Override
    public <T> void set(String columnName, T value, User user) {
        set(columnName, value, user.getCompanyId(), user.getUserId());
    }

    @Override
    public <T> T get(String columnName, long companyId, long userId) {
        try {
            return (T) expandoUtil.getExpando(companyId, USER_CLASSNAME, columnName, userId);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public <T> T get(String columnName, User user) {
        try {
            return get(columnName, user.getCompanyId(), user.getUserId());
        } catch (Exception ex) {
            return null;
        }
    }

    private static final String USER_CLASSNAME = User.class.getName();
}
