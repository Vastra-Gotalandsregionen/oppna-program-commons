package se.vgregion.liferay.expando;

import com.liferay.portal.model.User;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:29
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface UserExpandoHelper {

    <T> void set(String columnName, T value, long companyId, long userId);
    <T> void set(String columnName, T value, User user);

    <T> T get(String columnName, long companyId, long userId);
    <T> T get(String columnName, User user);

}
