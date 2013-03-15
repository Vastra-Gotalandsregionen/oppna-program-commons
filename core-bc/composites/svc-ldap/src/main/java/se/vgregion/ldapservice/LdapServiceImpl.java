/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.ldapservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.*;

public class LdapServiceImpl implements LdapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapServiceImpl.class);

    private String _bindDN;
    private String _bindPw;
    private String _bindUrl;

    private String[] _defaultReadAttrs;
    private String[] _defaultAddAttrs;

    private Object[] _objectClasses;

    protected String base;
    protected Properties properties;
    private Hashtable env;

    public Properties getProperties() {
        return properties;
    }

    /**
     * Default zero-arg constructor
     */
    public LdapServiceImpl() {

    }

    public LdapServiceImpl(Properties p) {

        this(p.getProperty("BIND_URL"), p.getProperty("BIND_DN"), p.getProperty("BIND_PW"), new String[]{},
                new String[]{}, new Object[]{});
        this.properties = p;
        this.base = p.getProperty("BASE");

    }

    private LdapServiceImpl(String bindUrl, String bindDN, String bindPassword, String[] readAttrs,
                            String[] updateAttrs, Object[] objClasses) {

        _bindDN = bindDN;
        _bindUrl = bindUrl;
        _bindPw = bindPassword;
        _defaultReadAttrs = readAttrs;
        _objectClasses = objClasses;

        _defaultAddAttrs = new String[updateAttrs.length + 4];
        _defaultAddAttrs[0] = "objectclass";
        _defaultAddAttrs[1] = "cn";
        _defaultAddAttrs[2] = "sn";
        _defaultAddAttrs[3] = "mail";

        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, _bindUrl);
        env.put("com.sun.jndi.ldap.connect.pool", "true");
        if (_bindDN != null) {
            env.put(Context.SECURITY_PRINCIPAL, _bindDN);
            env.put(Context.SECURITY_CREDENTIALS, _bindPw);
        }
    }

    private DirContext getBaseContext() {
        try {
            return new InitialDirContext(env);
        } catch (Exception e) {
            throw new RuntimeException("Bind failed", e);
        }
    }

    public LdapUser[] search(String base, String filter, String[] attributes) {
        this._defaultReadAttrs = attributes;
        return this.search(base, filter);
    }

    public LdapUser[] search(String base, String filter) {
        if (base == null) {
            base = this.base;
        }
        DirContext dirContext = null;
        try {
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            if (_defaultReadAttrs.length > 0) {
                sc.setReturningAttributes(_defaultReadAttrs);
            }
            dirContext = getBaseContext();
            NamingEnumeration results = dirContext.search(base, filter, sc);
            List entries = new ArrayList();

            int j = 0;
            while (results.hasMoreElements()) {
                SearchResult oneRes = (SearchResult) results.nextElement();
                entries.add(new LdapUserEntryImpl(base, oneRes));
            }
            LdapUser[] res = new LdapUser[entries.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = (LdapUser) entries.get(i);
            }
            return res;

        } catch (Exception e) {
            throw new RuntimeException("Search failed: base=" + base + " filter=" + filter, e);
        } finally {
            closeContext(dirContext);
        }
    }

    public LdapUser getLdapUser(String base, String filter, String[] attributes) {
        this._defaultReadAttrs = attributes;
        return this.getLdapUser(base, filter);
    }

    public LdapUser getLdapUser(String base, String filter) {
        if (base == null) {
            base = this.base;
        }
        DirContext dirContext = null;
        try {
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            if (_defaultReadAttrs.length > 0) {
                sc.setReturningAttributes(_defaultReadAttrs);
            }
            dirContext = getBaseContext();
            NamingEnumeration results = dirContext.search(base, filter, sc);
            List entries = new ArrayList();

            while (results.hasMore()) {
                SearchResult oneRes = (SearchResult) results.next();
                entries.add(new LdapUserEntryImpl(base, oneRes));
            }

            if (entries.size() > 1) {
                throw new RuntimeException("Entry is not unique: " + filter);
            } else if (entries.size() == 0) {
                return null;
            }

            return (LdapUser) entries.get(0);

        } catch (Exception e) {
            throw new RuntimeException("Search failed: base=" + base + " filter=" + filter, e);
        } finally {
            closeContext(dirContext);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see se.vgregion.ldapservice.LdapService#addLdapUser(java.lang.String, java.util.HashMap)
     */
    public boolean addLdapUser(String context, HashMap<String, String> attributes) {

        DirContext dirContext = null;
        try {

            int x = 0;
            LdapUser e = this.newUser(context);

            String[] addAttrs = new String[attributes.size() + 1];
            addAttrs[x++] = "objectclass";
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                String attName = entry.getKey();
                addAttrs[x++] = attName;
                String attValue = entry.getValue();
                e.setAttributeValue(attName, attValue);
            }

            e.addAttributeValue("objectclass", "vgrUser");
            e.addAttributeValue("objectclass", "inetOrgPerson");

            Attributes attrs = ((LdapUserEntryImpl) e).getAttributes(addAttrs);
            String dn = e.getDn();
            dirContext = getBaseContext();
            dirContext.createSubcontext(dn, attrs);
            return true;
        } catch (Exception ex) {
            throw new RuntimeException("Add failed", ex);
        } finally {
            closeContext(dirContext);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see se.vgregion.ldapservice.LdapService#modifyLdapUser(se.vgregion.ldapservice.LdapUser, java.util.HashMap)
     */
    public boolean modifyLdapUser(LdapUser e, HashMap<String, String> modifyAttributes) {
        DirContext dirContext = null;
        try {
            int x = 0;
            String[] modifyAttrs = new String[modifyAttributes.size() + 1];
            for (Map.Entry<String, String> entry : modifyAttributes.entrySet()) {
                String attName = entry.getKey();
                modifyAttrs[x++] = attName;
                e.setAttributeValue(attName, entry.getValue());
            }

            Attributes attrs = ((LdapUserEntryImpl) e).getAttributes(modifyAttrs);
            dirContext = getBaseContext();
            dirContext.modifyAttributes(e.getDn(), InitialDirContext.REPLACE_ATTRIBUTE, attrs);
            return true;
        } catch (Exception ex) {
            throw new RuntimeException("Modify failed", ex);
        } finally {
            closeContext(dirContext);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see se.vgregion.ldapservice.LdapService#deleteLdapUser(se.vgregion.ldapservice.LdapUser)
     */
    public boolean deleteLdapUser(LdapUser e) {
        DirContext dirContext = null;
        try {
            dirContext = getBaseContext();
            dirContext.destroySubcontext(e.getDn());
            return true;
        } catch (Exception ex) {
            throw new RuntimeException("Delete failed", ex);
        } finally {
            closeContext(dirContext);
        }

    }

    @Override
    public LdapUser getLdapUserByUid(String base, String uid) {
        throw new UnsupportedOperationException("Not implemented in LdapServiceImpl, use simple ldap service");
    }

    public static String dumpSearchRes(LdapUser[] res) {
        StringBuffer buf = new StringBuffer(256);
        for (int i = 0; i < res.length; i++) {
            buf.append(res[i]);
        }
        return buf.toString();
    }

    private static boolean arrayContains(String[] a, String val) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == null) {
                if (val == null) {
                    return true;
                }
            } else {
                if (a[i].equals(val)) {
                    return true;
                }
            }

        }
        return false;
    }

    public static String dumpAttrMap(Map m) {
        StringBuffer buf = new StringBuffer(256);
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            List values = (List) m.get(key);
            buf.append("   " + key + ": |");
            Iterator it2 = values.iterator();
            while (it2.hasNext()) {
                String oneVal = (String) it2.next();
                buf.append(oneVal + "|");
            }
            buf.append("\n");
        }
        return buf.toString();
    }

    private LdapUser newUser(String rdn) {
        LdapUser e = new LdapUserEntryImpl(rdn);
        e.setAttributeValue("objectclass", _objectClasses);
        return e;
    }

    public LdapUser getLdapUserByUid(String uid) {
        throw new UnsupportedOperationException("Not implemented in LdapServiceImpl, use simple ldap service");

    }

    private void closeContext(DirContext dirContext) {
        if (dirContext != null) {
            try {
                dirContext.close();
            } catch (NamingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
