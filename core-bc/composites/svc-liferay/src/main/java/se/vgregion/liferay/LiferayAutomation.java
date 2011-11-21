package se.vgregion.liferay;

import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-20 16:23
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class LiferayAutomation {
    /**
     * Date format used for/when parsing date strings.
     */
    protected final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    @Autowired
    private Map<Long, String> sysadminUsers;

    @Autowired
    private UserLocalService userLocalService;

    public User lookupSysadmin(long companyId) throws Exception {
        String systemUserScreenName = sysadminUsers.get(companyId);
        if (systemUserScreenName == null) {
            String msg = String.format("SystemUser is not configured for companyId [%s]", companyId);
            throw new Exception(msg);
        }

        User systemUser = userLocalService.getUserByScreenName(companyId, systemUserScreenName);
        if (systemUser == null) {
            String msg = String.format("SystemUser [%s] cannot be found on companyId [%s]",
                    systemUserScreenName, companyId);
            throw new Exception(msg);
        }
        return systemUser;
    }

    public String autoCreateDescription() {
        Date now = new Date();
        return String.format("Created automatically on %s", dateFormat.format(now));
    }

    public String autoRenameDescription(String oldName, String newName) {
        Date now = new Date();
        return String.format("Name changed from [%s] to [%s] on %s", oldName, newName, dateFormat.format(now));
    }
}
