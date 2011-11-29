package se.vgregion.liferay.expando;

import com.liferay.portlet.expando.model.ExpandoColumn;

import com.liferay.portal.model.Company;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:29
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class CompanyExpandoHelperImpl implements CompanyExpandoHelper {

    @Autowired
    private ExpandoUtil expandoUtil;

	@Override
    public void set(String columnName, String value, long companyId) {
        expandoUtil.setExpando(COMPANY_CLASSNAME, columnName, value, companyId, companyId,
                ExpandoUtil.Mode.AUTO_CREATE);
	}

    @Override
    public String get(String columnName, long companyId) {
        Object value = expandoUtil.getExpando(companyId, COMPANY_CLASSNAME, columnName, companyId);
        if (value == null) {
            return "";
        }
        return (String)value;
    }

    @Override
    public List<ExpandoColumn> getAll(long companyId) {
        return expandoUtil.getAllExpando(COMPANY_CLASSNAME, companyId);
    }

    @Override
    public void delete(long companyId, String columnName) {
        expandoUtil.deleteExpando(COMPANY_CLASSNAME, columnName, companyId);
    }

	private static final String COMPANY_CLASSNAME = Company.class.getName();
}
