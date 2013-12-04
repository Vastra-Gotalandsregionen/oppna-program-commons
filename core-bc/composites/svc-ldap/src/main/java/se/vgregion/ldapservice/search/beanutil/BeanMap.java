package se.vgregion.ldapservice.search.beanutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.*;

public class BeanMap implements Map<String, Object> {

    public final static Map<Class<?>, Object> defaultPrimitiveValues = new HashMap<Class<?>, Object>();

    protected final Converters converters = new Converters();

    static {
        defaultPrimitiveValues.put(Byte.TYPE, (byte) 0);
        defaultPrimitiveValues.put(Short.TYPE, (short) 0);
        defaultPrimitiveValues.put(Integer.TYPE, 0);
        defaultPrimitiveValues.put(Long.TYPE, 0l);
        defaultPrimitiveValues.put(Float.TYPE, 0f);
        defaultPrimitiveValues.put(Double.TYPE, 0d);
        defaultPrimitiveValues.put(Boolean.TYPE, false);
        defaultPrimitiveValues.put(Character.TYPE, (char) 0);
    }

    protected Object bean;

    protected BeanInfo beanInfo;

    protected Map<String, PropertyDescriptor> properties;

    protected Set<String> keys;

    private final Set<Entry<String, Object>> entries;

    /**
     * Makes an instance.
     * @param bean the very object that is to be wrapped by the bm.
     */
    public BeanMap(Object bean) {
        this.bean = bean;
        beanInfo = MetaHelp.getBeanInfo(bean.getClass());
        properties = MetaHelp.getDescriptors(beanInfo);
        keys = Collections.unmodifiableSet(properties.keySet());
        entries = initEntries();
    }

    private Set<Entry<String, Object>> initEntries() {
        Set<Entry<String, Object>> result = new HashSet<Entry<String, Object>>();
        for (PropertyDescriptor pd : properties.values()) {
            result.add(new BeanEntry(pd.getName()));
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        for (PropertyDescriptor pd : getWriteableDescs()) {
            Class<?> type = pd.getPropertyType();
            if (type.isPrimitive()) {
                put(pd.getName(), defaultPrimitiveValues.get(type));
            } else {
                put(pd.getName(), null);
            }
        }
    }

    private Set<PropertyDescriptor> getWriteableDescs() {
        Set<PropertyDescriptor> result = new HashSet<PropertyDescriptor>();
        for (PropertyDescriptor pd : properties.values()) {
            if (pd.getWriteMethod() != null) {
                result.add(pd);
            }
        }
        return result;
    }

    private PropertyDescriptor getPropertyDesc(String key) {
        return properties.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        return keys.contains(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return entries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(Object key) {
        String name = (String) key;
        PropertyDescriptor pd = properties.get(name);
        try {
            return pd.getReadMethod().invoke(bean);
        } catch (Exception e) {
            throw new RuntimeException("Error for key " + key + " on object " + bean + " and property desc " + pd, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> keySet() {
        return keys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object put(String key, Object value) {
        PropertyDescriptor pd = properties.get(key);
        Object oldValue = get(key);
        try {
            value = mayConvertValueBeforePut(pd, value);
            pd.getWriteMethod().invoke(bean, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return oldValue;
    }

    private Object mayConvertValueBeforePut(PropertyDescriptor descriptor, Object value) {
        Class<?> type = descriptor.getPropertyType();
        if (value == null) {
            if (type.isPrimitive()) {
                return defaultPrimitiveValues.get(type);
            } else {
                return null;
            }
        }
        if (!value.getClass().isAssignableFrom(type)) {
            return converters.convert(type, value);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends String, ?> m) {
        for (String key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    /*
    public void tryPutAll(Map<? extends String, ? extends Object> m) {
        for (String key : m.keySet()) {
            try {
                put(key, m.get(key));
            } catch (Exception e) {
                LOGGER.error("Misslyckades med " + key, e);
            }
        }
    }
    */

    @Override
    public Object remove(Object key) {
        return put((String) key, null);
    }

    @Override
    public int size() {
        return beanInfo.getPropertyDescriptors().length;
    }

    @Override
    public Collection<Object> values() {
        final Collection<Object> result = new HashSet<Object>();
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            result.add(get(pd.getName()));
        }
        return result;
    }

    @Override
    public String toString() {
        TreeMap<String, Object> tm = new TreeMap<String, Object>();
        for (String key : keySet()) {
            try {
                tm.put(key, get(key) + "");
            } catch (Exception e) {
                tm.put(key, "err:" + e.getClass().getSimpleName());
            }
        }
        return tm.toString();
    }

    /**
     * To know if a certain property have a public setter method - that will be used when setting a value on the map.
     * Good to have since trying to set a read-only-value causes an error.
     * @param key name of the property to check.
     * @return true if the value can be set.
     */
    public boolean isWritable(String key) {
        if (!keySet().contains(key)) {
            return false;
        }
        return getPropertyDesc(key).getWriteMethod() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BeanMap)) return false;
        if (this == obj) {
            return true;
        }

        BeanMap bm = (BeanMap) obj;
        if (!(bm.keySet().equals(keySet()))) {
            return false;
        }
        for (String key : keySet()) {
            if (!same(get(key), bm.get(key))) {
                return false;
            }
        }

        return true;
    }

    private boolean same(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }


    class BeanEntry implements Entry<String, Object> {

        final String key;

        BeanEntry(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return BeanMap.this.get(key);
        }

        @Override
        public Object setValue(Object value) {
            return BeanMap.this.put(key, value);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return same(this, obj);
        }

    }

}
