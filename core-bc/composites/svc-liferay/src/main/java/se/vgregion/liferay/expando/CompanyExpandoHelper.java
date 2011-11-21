package se.vgregion.liferay.expando;

import com.liferay.portlet.expando.model.ExpandoColumn;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:29
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface CompanyExpandoHelper {
    void set(String columnName, String value, long companyId);

    String get(String columnName, long companyId);

    List<ExpandoColumn> getAll(long companyId);

    void delete(long companyId, String columnName);
}
