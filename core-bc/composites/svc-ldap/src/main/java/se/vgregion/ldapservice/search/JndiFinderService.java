/**
 * Copyright 2009 Västa Götalandsregionen
 * <p>
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of version 2.1 of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307  USA
 */
package se.vgregion.ldapservice.search;

import se.vgregion.ldapservice.search.beanutil.BeanMap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Accessor/search-class for a ldap/jndi database.
 * It uses no spring (ldap templates) for its connection.
 */
public class JndiFinderService {

    private String bindDn;

    private String bindPassword;

    private String bindUrl;

    private String searchBase;

    private DirContext context;

    /**
     * Creates an new instance using connection properties.
     * @param bindUrl the url to the jndi/ldap server.
     * @param bindDn what user to bind with.
     * @param bindPassword secret to validate connection rights.
     * @param searchBase from where in the db to perform the queries.
     */
    public JndiFinderService(String bindUrl, String bindDn, String bindPassword, String searchBase) {
        this.bindDn = bindDn;
        this.bindUrl = bindUrl;
        this.bindPassword = bindPassword;
        this.searchBase = searchBase;
    }

    /**
     * Creates an new instance using the {@link Path} to a properties-file that specifies BIND_URL, BIND_DN, BIND_PW
     * and BASE (search-base that is).
     * @param path path to properties file to be read.
     */
    public JndiFinderService(Path path) {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(path));
            bindUrl = properties.getProperty("BIND_URL");
            bindDn = properties.getProperty("BIND_DN");
            bindPassword = properties.getProperty("BIND_PW");
            searchBase = properties.getProperty("BASE");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void bind() {
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, bindUrl);
            if (bindDn != null) {
                env.put(Context.SECURITY_PRINCIPAL, bindDn);
                env.put(Context.SECURITY_CREDENTIALS, bindPassword);
            }
            context = new InitialDirContext(env);

        } catch (Exception e) {
            throw new RuntimeException("Bind failed", e);
        }
    }

    private String toAndCondition(Object obj) {
        StringBuilder filter = new StringBuilder();
        BeanMap bm = new BeanMap(obj);
        Class type = obj.getClass();
        for (Object entryObj : bm.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entryObj;
            String property = entry.getKey();
            if (bm.isWritable(property)) {
                Object value = entry.getValue();
                if (value != null && !"".equals(value.toString().trim())) {
                    String ldapPropertyName = getPlainNameOrExplicit(type, property);
                    filter.append(newAttributeFilter(ldapPropertyName, value.toString()));
                }
            }
        }
        return "(&" + filter.toString() + ")";
    }

    private String newAttributeFilter(final String name, final String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(name);
        sb.append("=");
        sb.append(value);
        sb.append(")");
        return sb.toString();
    }

    static String getPlainNameOrExplicit(Class type, String propertyName) {
        try {
            return getPlainNameOrExplicitImpl(type, propertyName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static String getPlainNameOrExplicitImpl(Class type, String propertyName) throws NoSuchFieldException {
        Field field = getField(type, propertyName);
        Annotation annotation = field.getAnnotation(ExplicitLdapName.class);
        if (annotation == null) {
            return propertyName;
        }
        ExplicitLdapName explicitLdapName = (ExplicitLdapName) annotation;
        return explicitLdapName.value();
    }

    static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    /**
     * Finds data from the jndi/ldap server. Provide a structure (class instance) with the data to use as search criteria
     * and gets the answer as a list with the same format (class type) as the criteria.
     *
     * @param byExample holds properties that (could) match fields in the db by the operator '=' or 'like' (in conjunction
     *               with having a '*' character in a String value).
     * @param <T>    type of the param and type of the answers inside the resulting list.
     * @return a list of search hits.
     */
    public <T> List<T> find(T byExample) {
        return findImp(byExample);
    }

    private <T> List<T> findImp(T byExample) {
        List<Map<String, Object>> findings = search(searchBase, toAndCondition(byExample));
        List<T> result = new ArrayList<T>();
        final BeanAttributesMapper<T> mapper = new BeanAttributesMapper(byExample.getClass());
        for (Map<String, Object> entry : findings) {
            result.add(mapper.mapFromAttributes(entry));
        }
        return result;
    }

    private DirContext getBaseContext() {
        if (context == null) {
            bind();
        }
        return context;
    }

    private List<Map<String, Object>> search(String base, String filter) {
        try {
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            sc.setDerefLinkFlag(false);
            NamingEnumeration results = getBaseContext().search(base, filter, sc);

            List<Map<String, Object>> result = new ArrayList<>();

            while (results.hasMore()) {
                Map<String, Object> item = new HashMap<>();
                result.add(item);
                SearchResult oneRes = (SearchResult) results.next();
                NamingEnumeration<? extends Attribute> fields = (oneRes.getAttributes().getAll());
                for (Attribute a = fields.next(); fields.hasMore(); a = fields.next()) {
                    item.put(a.getID(), a.get());
                }
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Search failed: searchBase=" + base + " filter=" + filter, e);
        }
    }

}
