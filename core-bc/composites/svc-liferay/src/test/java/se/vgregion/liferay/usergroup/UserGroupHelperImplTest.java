package se.vgregion.liferay.usergroup;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.UserGroupLocalService;
import com.liferay.portal.service.UserLocalService;
import org.apache.log4j.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import se.vgregion.liferay.LiferayAutomation;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-21 17:05
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class UserGroupHelperImplTest {
    private UserGroupHelper userGroupHelper;

    private LiferayAutomation liferayAutomation;

    private UserGroupLocalService userGroupLocalService;

    private UserLocalService userLocalService;

    private static final String EOL = System.getProperty("line.separator");

    @Before
    public void setup() {
        liferayAutomation = mock(LiferayAutomation.class);
        userGroupLocalService = mock(UserGroupLocalService.class);
        userLocalService = mock(UserLocalService.class);

        userGroupHelper = new UserGroupHelperImpl();

        ReflectionTestUtils.setField(userGroupHelper, "liferayAutomation", liferayAutomation);
        ReflectionTestUtils.setField(userGroupHelper, "userGroupLocalService", userGroupLocalService);
        ReflectionTestUtils.setField(userGroupHelper, "userLocalService", userLocalService);
    }

    @Test
    public void testAddUserToGroup() throws Exception {

        User sysadmin = mock(User.class);
        when(liferayAutomation.lookupSysadmin(anyLong())).thenReturn(sysadmin);

        UserGroup userGroup = mock(UserGroup.class);
        when(userGroup.getUserGroupId()).thenReturn(1L);
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(2L);
        userGroupHelper.addUserToGroup(userGroup, user);

        verify(userLocalService).addUserGroupUsers(1L, new long[]{2L});
    }

    @Test
    public void testAddUserToGroupFail() throws Exception {
        // given
        final StringWriter writer = setupLogger(UserGroupHelperImpl.class, Level.WARN);

        User sysadmin = mock(User.class);
        when(liferayAutomation.lookupSysadmin(anyLong())).thenReturn(sysadmin);

        UserGroup userGroup = mock(UserGroup.class);
        when(userGroup.getUserGroupId()).thenReturn(1L);
        when(userGroup.getName()).thenReturn("UG");
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(2L);
        when(user.getScreenName()).thenReturn("U");

        doThrow(new SystemException("ERROR")).when(userLocalService).
                addUserGroupUsers(1L, new long[]{2L});

        userGroupHelper.addUserToGroup(userGroup, user);

        verify(userLocalService).addUserGroupUsers(1L, new long[]{2L});

        // then
        String[] logMessages = writer.toString().split(EOL);
        assertEquals("WARN - Failed to add users [U] to UserGroup [UG]", logMessages[0]);
    }

    @Test
    public void testRemoveUserFromGroup1() throws Exception {

    }

    @Test
    public void testRemoveUserFromGroup2() throws Exception {

    }

    @Test
    public void testFindByName() throws Exception {

    }

    @Test
    public void testIsMember() throws Exception {

    }

    @Test
    public void testCreateIfNeeded() throws Exception {

    }

    @Test
    public void testRename() throws Exception {

    }

    @Test
    public void testMoveUsers() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }


    private StringWriter setupLogger(Class loggerClass, Level level) {
        Logger logger = Logger.getLogger(loggerClass);
        logger.setLevel(level);

        final StringWriter writer = new StringWriter();
        Appender appender = new WriterAppender(new SimpleLayout(), writer);
        logger.addAppender(appender);
        return writer;
    }

}
