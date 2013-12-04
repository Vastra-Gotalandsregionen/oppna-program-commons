package se.vgregion.ldapservice.search.beanutil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class MetaHelp {

    private static Map<Class<?>, BeanInfo> beanInfoCache = new HashMap<Class<?>, BeanInfo>();

    private static Map<BeanInfo, Map<String, PropertyDescriptor>> descriptors = new HashMap<BeanInfo, Map<String, PropertyDescriptor>>();

    public static BeanInfo getBeanInfo(Class<?> clazz) {
        if (clazz == null) throw new IllegalArgumentException("Argument cannot be null.");
        BeanInfo result = beanInfoCache.get(clazz);
        if (result != null) return result;
        try {
            result = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        beanInfoCache.put(clazz, result);

        return result;
    }

    public static Map<String, PropertyDescriptor> getDescriptors(BeanInfo key) {
        Map<String, PropertyDescriptor> result = descriptors.get(key);
        if (result == null) {
            result = new HashMap<String, PropertyDescriptor>();
            for (PropertyDescriptor pd : key.getPropertyDescriptors()) {
                result.put(pd.getName(), pd);
            }
        }
        return result;
    }

    public static Field getField(Class<?> clazz, String name) {
        Field result = null;
        try {
            result = clazz.getDeclaredField(name);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            if (clazz.equals(Object.class)) return null;
            return getField(clazz.getSuperclass(), name);
        }
        return result;
    }

    public static Set<String> getFieldNamesByAnnotations(Class<?> beanClass, Set<Class<? extends Annotation>> lookingFor) {
        Set<String> result = new HashSet<String>();
        for (Field field : beanClass.getDeclaredFields()) {
            for (Class<? extends Annotation> annotation : lookingFor) {
                if (field.isAnnotationPresent(annotation)) {
                    result.add(field.getName());
                }
            }
        }
        return result;
    }

    public static Set<String> getFieldNamesByAnnotations(Class<?> beanClass, Class<? extends Annotation>... lookingFor) {
        return getFieldNamesByAnnotations(beanClass, new HashSet<Class<? extends Annotation>>(Arrays.asList(lookingFor)));
    }
}
