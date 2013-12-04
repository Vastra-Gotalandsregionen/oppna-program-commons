/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.vgregion.ldapservice.search.beanutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author clalul
 */
public class Converters {

    private static final Map<Class<?>, Converter> defaultConverters = new HashMap<Class<?>, Converter>();
    final Map<Class<?>, Converter> converters = new HashMap<Class<?>, Converter>();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Converters() {
        converters.putAll(defaultConverters);
    }

    static {
        Converter con;

        defaultConverters.put(Boolean.TYPE, con = new Converter() {

            public Object convert(Object input) {
                return Boolean.valueOf(input.toString());
            }
        });
        defaultConverters.put(Boolean.class, con);

        defaultConverters.put(Character.TYPE, con = new Converter() {

            public Object convert(Object input) {
                return input.toString().charAt(0);
            }
        });
        defaultConverters.put(Character.class, con);

        defaultConverters.put(Byte.TYPE, con = new Converter() {

            public Object convert(Object input) {
                return Byte.valueOf(input.toString());
            }
        });
        defaultConverters.put(Byte.class, con);

        defaultConverters.put(Short.TYPE, con = new Converter() {

            public Object convert(Object input) {
                return Short.valueOf(input.toString());
            }
        });
        defaultConverters.put(Short.class, con);

        defaultConverters.put(Integer.TYPE, con = new Converter() {

            public Object convert(Object input) {
                return Integer.valueOf(input.toString());
            }
        });
        defaultConverters.put(Integer.class, con);

        defaultConverters.put(Long.TYPE, con = new Converter() {

            public Object convert(Object input) {
                return Long.valueOf(input.toString());
            }
        });
        defaultConverters.put(Long.class, con);

        defaultConverters.put(Float.TYPE, con = new Converter() {

            public Object convert(Object input) {
                return Float.valueOf(input.toString());
            }
        });
        defaultConverters.put(Float.class, con);

        defaultConverters.put(Double.TYPE, con = new Converter() {
            public Object convert(Object input) {
                return Double.valueOf(input.toString());
            }
        });
        defaultConverters.put(Double.class, con);

        defaultConverters.put(Date.class, new Converter() {
            public Object convert(Object input) {
                try {
                    if (!(input instanceof Date)) {
                        if (input == null || "".equals(input.toString().trim())) {
                            return null;
                        }
                        return sdf.parse(input.toString());
                    }
                } catch (ParseException pe) {
                    throw new RuntimeException(pe);
                }
                return input;
            }
        });

        defaultConverters.put(java.sql.Date.class, new Converter() {
            public Object convert(Object input) {
                try {
                    if (!(input instanceof java.sql.Date)) {
                        if (input == null || "".equals(input.toString().trim())) {
                            return null;
                        }
                        Date date = sdf.parse(input.toString());
                        return new java.sql.Date(date.getTime());
                    }
                } catch (ParseException pe) {

                }
                return input;
            }
        });
        defaultConverters.put(String.class, con = new Converter() {
            public Object convert(Object input) {
                return String.valueOf(input);
            }
        });
        defaultConverters.put(Double.class, con);
    }


    public static Map<Class<?>, Converter> getDefaultConverters() {
        return defaultConverters;
    }

    public Object convert(Class<?> clazz, Object value) {
        if (defaultConverters.containsKey(clazz)) {
            return converters.get(clazz).convert(value);
        } else {
            return value;
        }
    }
}
