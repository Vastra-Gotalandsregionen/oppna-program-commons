package se.vgregion.ldapservice.search;

import se.vgregion.ldapservice.search.beanutil.BeanMap;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Super class for bean describing person information in various ldap-db:s. Provides basic bean behaviour: toString
 * hashCode and equals.
 */
public abstract class AbstractPerson implements Serializable {

    private final BeanMap bm = new BeanMap(this);

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        Map<String, Object> map = new TreeMap<String, Object>(bm);
        StringBuilder sb = new StringBuilder();

        for (Map.Entry entry : map.entrySet()) {
            Object value = entry.getValue();
            boolean modded = false;
            if (value != null) {
                Class clazz = value.getClass();
                if (clazz.isArray()) {
                    if (Byte.class.equals(clazz.getComponentType()) || byte.class.equals(clazz.getComponentType())) {
                        byte[] bytes = (byte[]) value;
                        entry.setValue(toString(bytes));
                        modded = true;
                    }
                }
                if (!modded) {
                    if (value instanceof String) {
                        String str = (String) value;
                        entry.setValue(toString(str.getBytes()));
                    }
                }
            }
            sb.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        return sb.toString();
    }

    private String toString(byte[] bytes) {
        try {
            bytes = new String(bytes).getBytes("utf-8");
            return new String(bytes).replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractPerson)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        BeanMap otherMap = new BeanMap(obj);
        return bm.equals(otherMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return bm.hashCode();
    }
}
