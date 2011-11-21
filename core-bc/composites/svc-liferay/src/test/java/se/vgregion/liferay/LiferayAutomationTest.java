package se.vgregion.liferay;

import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-21 10:44
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class LiferayAutomationTest {
    private LiferayAutomation liferayAutomation;
    private UserLocalService userLocalService;

    @Before
    public void setup() {
        userLocalService = mock(UserLocalService.class);

        liferayAutomation = new LiferayAutomation();
        ReflectionTestUtils.setField(liferayAutomation, "propsUtilWrapper", new PropsUtilWrapperStub());

        ReflectionTestUtils.setField(liferayAutomation, "userLocalService", userLocalService);


    }

    @Test
    public void testLookupSysadmin() throws Exception {
        User sysadmin1 = mock(User.class);
        when(userLocalService.getUserByScreenName(eq(12345L), eq("lif1"))).thenReturn(sysadmin1);

        User result = liferayAutomation.lookupSysadmin(12345L);

        assertSame(result, sysadmin1);
    }

    @Test(expected = Exception.class)
    public void testLookupSysadminFailed() throws Exception {
        when(userLocalService.getUserByScreenName(eq(12345L), eq("lif1"))).thenReturn(null);

        User result = liferayAutomation.lookupSysadmin(12345L);
    }

    @Test(expected = Exception.class)
    public void testLookupSysadminFailedCompany() throws Exception {
        User result = liferayAutomation.lookupSysadmin(123L);
    }

    @Test
    public void testAutoCreateDescription() throws Exception {
        String msg = liferayAutomation.autoCreateDescription();
        assertEquals("Created automatically on ", msg.substring(0, msg.length() - 16));
    }

    @Test
    public void testAutoRenameDescription() throws Exception {
        String msg = liferayAutomation.autoRenameDescription("oldName", "newName");

        assertEquals("Name changed from [oldName] to [newName] on ", msg.substring(0, msg.length() - 16));

    }

    public class PropsUtilWrapperStub extends PropsUtilWrapper {
        @Override
        public String[] getArray(String key) {
            return new String[]{"12345;lif1", "54321;lif2"};
        }
    }
}
