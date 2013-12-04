package se.vgregion.ldapservice.search;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.LikeFilter;
import se.vgregion.ldapservice.search.beanutil.BeanMap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * Capablity to search in a ldap server on any field(s) and getting it back. The name of the fields that is used in
 * the search and that are present on the resulting beans are based the bean used for searching. What you search with
 * you will also get back, packaging the result in beans inside a list.
 * <p/>
 * Example of initilizating:
 * <p/>
 * <code>
 * <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * xmlns:context="http://www.springframework.org/schema/context"
 * xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
 * http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd">
 * <p/>
 * <p/>
 * <context:property-placeholder
 * location="file:${user.home}/.[your-app-config-dir]/application.properties"
 * ignore-resource-not-found="true" ignore-unresolvable="true" />
 * <p/>
 * <bean id="vgr.org.contextSource" class="org.springframework.ldap.pool.factory.PoolingContextSource">
 * <property name="contextSource" ref="vgr.org.contextSourceTarget" />
 * <property name="dirContextValidator" ref="vgr.org.dirContextValidator" />
 * <property name="testOnBorrow" value="true" />
 * <property name="testWhileIdle" value="true" />
 * </bean>
 * <p/>
 * <bean id="vgr.org.dirContextValidator" class="org.springframework.ldap.pool.validation.DefaultDirContextValidator" />
 * <p/>
 * <bean id="vgr.org.contextSourceTarget" class="org.springframework.ldap.core.support.LdapContextSource">
 * <property name="url" value="${ldap.org.authentication.java.naming.provider.url}" />
 * <property name="userDn" value="${ldap.org.synchronization.java.naming.security.principal}" />
 * <property name="password" value="${ldap.org.synchronization.java.naming.security.credentials}" />
 * <property name="base" value="${ldap.org.synchronization.userSearchBase}" />
 * <property name="pooled" value="false" />
 * </bean>
 * <p/>
 * <bean id="vgr.org.ldapTemplate" class="org.springframework.ldap.core.LdapTemplate">
 * <property name="contextSource" ref="vgr.org.contextSource" />
 * </bean>
 * <p/>
 * <bean id="ldapService" class="se.vgregion.ldapservice.search.LdapFinderService">
 * <property name="ldapTemplate" ref="vgr.org.ldapTemplate" />
 * </bean>
 * <p/>
 * </beans>
 * </code>
 */
public class LdapFinderService {

    private LdapTemplate ldapTemplate;
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Finds data from the ldap server. Provide a structure (class instance) with the data to use as search criteria
     * and gets the answer as a list with the same format (class type) as the criteria.
     *
     * @param sample holds properties that (could) match fields in the db by the operator '=' or 'like' (in conjunction
     *               with having a '*' character in a String value).
     * @param <T>    type of the param and type of the answers inside the resulting list.
     * @return a list of search hits.
     */
    public <T> List<T> find(T sample) {
        return findImp(sample, newBeanAttributesMapper(sample.getClass()));
    }

    /**
     * Se se.vgregion.ldapservice.search.LdapFinderService#find(T) to understand this method. Except that it wraps
     * the return in a future object.
     * @param sample holds properties that (could) match fields in the db by the operator '=' or 'like' (in conjunction
     *               with having a '*' character in a String value).
     * @param <T>    type of the param and type of the answers inside the resulting list.
     * @return a list of search hits, wraped in a future object.
     */
    public <T> Future<List<T>> findFuture(final T sample) {
        return executor.submit(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                return find(sample);
            }
        });
    }

    private <T> List<T> findImp(T sample, final AttributesMapper mapper) {
        final Filter searchFilter = toAndCondition(sample);
        final SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*"});
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        return ldapTemplate.search(StringUtils.EMPTY, searchFilter.encode(), searchControls,
                mapper);
    }

    /**
     * Creates @org.springframework.ldap.core.AttributesMapper that maps values from ldap search to a bean.
     *
     * @param type what type (of bean) to make the mapping functionality for.
     * @return A new instance of org.springframework.ldap.core.AttributesMapper.
     */
    public static AttributesMapper newBeanAttributesMapper(final Class type) {
        return new BeanAttributesMapper(type);
    }

    static String toBeanPropertyName(String name) {
        name = removeSignFrom(name, ";");
        name = removeSignFrom(name, "-");
        return name;
    }

    static String removeSignFrom(String beanPropertyName, String sign) {
        if (beanPropertyName.contains(sign)) {
            String[] parts = beanPropertyName.split(Pattern.quote(sign));
            StringBuilder sb = new StringBuilder(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                char head = Character.toUpperCase(parts[i].charAt(0));
                String tail = parts[i].substring(1);
                sb.append(head);
                sb.append(tail);
            }
            return sb.toString();
        }
        return beanPropertyName;
    }


    Filter newAttributeFilter(final String name, final String value) {
        Filter filter;
        if (value.contains("*")) {
            filter = new LikeFilter(name, value);
        } else {
            filter = new EqualsFilter(name, value);
        }
        return filter;
    }

    AndFilter toAndCondition(Object obj) {
        AndFilter filter = new AndFilter();
        BeanMap bm = new BeanMap(obj);
        Class type = obj.getClass();
        for (Object entryObj : bm.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entryObj;
            String property = entry.getKey();
            if (bm.isWritable(property)) {
                Object value = entry.getValue();
                if (value != null && !"".equals(value.toString().trim())) {
                    String ldapPropertyName = getPlainNameOrExplicit(type, property);
                    filter.and(newAttributeFilter(ldapPropertyName, value.toString()));
                }
            }
        }
        return filter;
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
     * Getter for ldapTemplate.
     * @return instance of mentioned var.
     */
    public LdapTemplate getLdapTemplate() {
        return ldapTemplate;
    }

    /**
     * Setter for ldapTemplate.
     * @param ldapTemplate the new value.
     */
    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public String toBeanText(String sampleCn) {
        WebLdapPerson sample = new WebLdapPerson();
        sample.setCn(sampleCn);
        StringBuilder sb = new StringBuilder();
        findImp(sample, newBeanToJavaCodeAttributesMapper(sb));
        return sb.toString();
    }

    private AttributesMapper newBeanToJavaCodeAttributesMapper(final StringBuilder sb) {
        return new BeanAttributesMapper(WebLdapPerson.class) {
            @Override
            public Object mapFromAttributes(Attributes attributes) throws NamingException {
                NamingEnumeration<? extends Attribute> all = attributes.getAll();
                while (all.hasMore()) {
                    Attribute attribute = all.next();

                    String name = toBeanPropertyName(attribute.getID());

                    Object value = attribute.get();
                    String className = "String";
                    if (value != null && !String.class.equals(value.getClass())) {
                        Class clazz = value.getClass();
                        if (clazz.isArray()) {
                            className = clazz.getComponentType().getName() + "[]";
                        } else {
                            className = value.getClass().getName();
                        }
                    }

                    sb.append("@ExplicitLdapName(\"" + attribute.getID() + "\")\n");
                    sb.append("private " + className + " " + name + ";\n\n");
                }
                return super.mapFromAttributes(attributes);
            }
        };
    }


    public static class BeanAttributesMapper implements AttributesMapper {

        private final Class type;

        public BeanAttributesMapper(Class type) {
            super();
            this.type = type;
        }

        @Override
        public Object mapFromAttributes(Attributes attributes) throws NamingException {
            try {
                return mapFromAttributesImpl(attributes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Object mapFromAttributesImpl(Attributes attributes) throws NamingException,
                IllegalAccessException, InstantiationException {
            Object result = type.newInstance();
            BeanMap bm = new BeanMap(result);
            NamingEnumeration<? extends Attribute> all = attributes.getAll();

            Map<String, String> actualValues = new HashMap<String, String>();

            while (all.hasMore()) {
                Attribute attribute = all.next();
                String name = toBeanPropertyName(attribute.getID());
                actualValues.put(name, attribute.getID());
                if (bm.containsKey(name) && bm.isWritable(name)) {
                    bm.put(name, attribute.get());
                }
            }
            return result;
        }
    }
}
