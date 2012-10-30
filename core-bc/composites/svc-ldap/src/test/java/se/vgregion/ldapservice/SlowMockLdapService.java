package se.vgregion.ldapservice;

import java.util.HashMap;
import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Patrik Bergström
 */
public class SlowMockLdapService implements LdapService {
    private long delayForGettingUser;

    public SlowMockLdapService(long delayForGettingUser) {
        this.delayForGettingUser = delayForGettingUser;
    }

    @Override
    public LdapUser[] search(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser[] search(String s, String s1, String[] strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser getLdapUser(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser getLdapUser(String s, String s1, String[] strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addLdapUser(String s, HashMap<String, String> stringStringHashMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean modifyLdapUser(LdapUser ldapUser, HashMap<String, String> stringStringHashMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteLdapUser(LdapUser ldapUser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser getLdapUserByUid(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LdapUser getLdapUserByUid(String s) {
        LdapUser ldapUser = mock(LdapUser.class);
        when(ldapUser.getAttributeValue("mailServer")).thenReturn("CN=liv,OU=epost,O=vgregion");

        try {
            Thread.sleep(delayForGettingUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ldapUser;
    }
}
