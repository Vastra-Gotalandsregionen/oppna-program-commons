package se.vgregion.ldapservice;

import java.util.HashMap;
import java.util.Properties;

/**
 * @author Patrik Bergström
 */
public class ReturnNullMockLdapService implements LdapService {

    public ReturnNullMockLdapService() {
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
        return null;
    }
}
