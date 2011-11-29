package se.vgregion.liferay.expando;

import com.liferay.portal.model.Group;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:29
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class CommunityExpandoHelperImpl implements CommunityExpandoHelper {

    @Autowired
    private ExpandoUtil expandoUtil;

    @Override
    public <T> void set(String columnName, T value, long companyId, long communityId) {
        expandoUtil.setExpando(COMMUNITY_CLASSNAME, columnName, value, companyId, communityId,
                ExpandoUtil.Mode.AUTO_CREATE);
    }

    @Override
    public <T> T get(String columnName, long companyId, long communityId) {
        try {
            return (T)expandoUtil.getExpando(companyId, COMMUNITY_CLASSNAME, columnName, communityId);
        } catch (Exception e) {
            return null;
        }
    }

    private final static String COMMUNITY_CLASSNAME = Group.class.getName();

}
