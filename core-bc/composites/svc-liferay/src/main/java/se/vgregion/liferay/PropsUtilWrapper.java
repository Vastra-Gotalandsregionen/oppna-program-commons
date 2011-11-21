package se.vgregion.liferay;

import com.liferay.portal.kernel.util.PropsUtil;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-21 16:11
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class PropsUtilWrapper {

    public String[] getArray(String key) {
        return PropsUtil.getArray(key);
    }
}
