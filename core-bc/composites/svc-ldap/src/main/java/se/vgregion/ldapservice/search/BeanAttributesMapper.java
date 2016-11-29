package se.vgregion.ldapservice.search;

import se.vgregion.ldapservice.search.beanutil.BeanMap;

import javax.naming.NamingException;
import java.util.Map;
import java.util.regex.Pattern;

public class BeanAttributesMapper<T> {

    private final Class<T> type;

    public BeanAttributesMapper(Class<T> type) {
        super();
        this.type = type;
    }

    public T mapFromAttributes(Map<String, Object> attributes) {
        try {
            return mapFromAttributesImpl(attributes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T mapFromAttributesImpl(Map<String, Object> attributes) throws NamingException,
            IllegalAccessException, InstantiationException {
        T result = type.newInstance();
        BeanMap bm = new BeanMap(result);

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String name = toBeanPropertyName(entry.getKey());
            if (bm.containsKey(name) && bm.isWritable(name)) {
                bm.put(name, entry.getValue());
            }
        }

        return result;
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

}