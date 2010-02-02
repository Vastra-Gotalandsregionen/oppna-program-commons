/**
 * ﻿Copyright 2009 Västra Götalandsregionen
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
 */

package se.vgr.ldapservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LdapServiceImpl implements LdapService {

    private String _bindDN;
    private String _bindPw;
    private String _bindUrl;

    private String[] _defaultReadAttrs;
    private String[] _defaultAddAttrs;

    private Object[] _objectClasses;

    private DirContext _ctx;
    protected String base;
    protected Properties properties;

    public Properties getProperties() {
        return properties;
    }
    
    /**
     * Default zero-arg constructor
     */
    public LdapServiceImpl() {
      
    }

    public LdapServiceImpl(Properties p) {

        this(p.getProperty("BIND_URL"), p.getProperty("BIND_DN"), p.getProperty("BIND_PW"), new String[] {},
                new String[] {}, new Object[] {});
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

    }

    private void bind() {
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, _bindUrl);
            if (_bindDN != null) {
                env.put(Context.SECURITY_PRINCIPAL, _bindDN);
                env.put(Context.SECURITY_CREDENTIALS, _bindPw);
            }
            _ctx = new InitialDirContext(env);

        }
        catch (Exception e) {
            throw new RuntimeException("Bind failed", e);
        }
    }

    private DirContext getBaseContext() {
        if (_ctx == null) {
            bind();
        }
        return _ctx;
    }

    public LdapUser[] search(String base, String filter, String[] attributes) {
        this._defaultReadAttrs = attributes;
        return this.search(base, filter);
    }

    public LdapUser[] search(String base, String filter) {
        if (base == null) {
            base = this.base;
        }
        try {
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            if (_defaultReadAttrs.length > 0) {
                sc.setReturningAttributes(_defaultReadAttrs);
            }
            NamingEnumeration results = getBaseContext().search(base, filter, sc);
            List entries = new ArrayList();

            while (results.hasMore()) {
                SearchResult oneRes = (SearchResult) results.next();
                entries.add(new LdapUserEntryImpl(base, oneRes));
            }
            LdapUser[] res = new LdapUser[entries.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = (LdapUser) entries.get(i);
            }
            return res;

        }
        catch (Exception e) {
            throw new RuntimeException("Search failed: base=" + base + " filter=" + filter, e);
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
        try {
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            if (_defaultReadAttrs.length > 0) {
                sc.setReturningAttributes(_defaultReadAttrs);
            }
            NamingEnumeration results = getBaseContext().search(base, filter, sc);
            List entries = new ArrayList();

            while (results.hasMore()) {
                SearchResult oneRes = (SearchResult) results.next();
                entries.add(new LdapUserEntryImpl(base, oneRes));
            }

            if (entries.size() > 1) {
                throw new RuntimeException("Entry is not unique: " + filter);
            }
            else if (entries.size() == 0) {
                return null;
            }

            return (LdapUser) entries.get(0);

        }
        catch (Exception e) {
            throw new RuntimeException("Search failed: base=" + base + " filter=" + filter, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see se.vgr.ldapservice.LdapService#addLdapUser(java.lang.String, java.util.HashMap)
     */
    public boolean addLdapUser(String context, HashMap<String, String> attributes) {

        try {

            int x = 0;
            LdapUser e = this.newUser(context);
            Iterator<String> it = attributes.keySet().iterator();

            String[] addAttrs = new String[attributes.size() + 1];
            addAttrs[x++] = "objectclass";
            while (it.hasNext()) {
                String attName = it.next();
                addAttrs[x++] = attName;
                String attValue = attributes.get(attName);
                e.setAttributeValue(attName, attValue);
            }

            e.addAttributeValue("objectclass", "vgrUser");
            e.addAttributeValue("objectclass", "inetOrgPerson");

            Attributes attrs = ((LdapUserEntryImpl) e).getAttributes(addAttrs);
            String dn = e.getDN();
            getBaseContext().createSubcontext(dn, attrs);
            return true;
        }
        catch (Exception ex) {
            throw new RuntimeException("Add failed", ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see se.vgr.ldapservice.LdapService#modifyLdapUser(se.vgr.ldapservice.LdapUser, java.util.HashMap)
     */
    public boolean modifyLdapUser(LdapUser e, HashMap<String, String> modifyAttributes) {
        try {
            Iterator<String> it = modifyAttributes.keySet().iterator();

            int x = 0;
            String[] modifyAttrs = new String[modifyAttributes.size() + 1];

            while (it.hasNext()) {
                String attName = it.next();
                modifyAttrs[x++] = attName;
                e.setAttributeValue(attName, modifyAttributes.get(attName));
            }

            Attributes attrs = ((LdapUserEntryImpl) e).getAttributes(modifyAttrs);
            getBaseContext().modifyAttributes(e.getDN(), InitialDirContext.REPLACE_ATTRIBUTE, attrs);
            return true;
        }
        catch (Exception ex) {
            throw new RuntimeException("Modify failed", ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see se.vgr.ldapservice.LdapService#deleteLdapUser(se.vgr.ldapservice.LdapUser)
     */
    public boolean deleteLdapUser(LdapUser e) {
        try {
            getBaseContext().destroySubcontext(e.getDN());
            return true;
        }
        catch (Exception ex) {
            throw new RuntimeException("Delete failed", ex);
        }

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
            }
            else {
                if (a[i].equals(val)) {
                    return true;
                }
            }

        }
        return false;
    }

    private static Attributes mapToAttrs(String[] attrNames, Map m) {
        BasicAttributes attrs = new BasicAttributes();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (arrayContains(attrNames, key)) {
                List values = (List) m.get(key);
                BasicAttribute oneAttr = new BasicAttribute(key);
                Iterator it2 = values.iterator();
                while (it2.hasNext()) {

                    Object oneVal = it2.next();
                    oneAttr.add(oneVal);
                }
                attrs.put(oneAttr);
            }
        }

        return attrs;
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
}
