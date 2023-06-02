package com.moon.portal.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
@Slf4j
public class PropertiesUtil {

    private PropertiesUtil() {
    }

    @Deprecated
    @SuppressWarnings("all")
    public static void properties2Object_(Properties properties, Object object, String prefix) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object value = properties.get(prefix + field.getName());
            if (value != null) {
                field.setAccessible(true);
                try {
                    field.set(object, value);
                } catch (IllegalAccessException e) {
                    log.warn("set config value error, the config name is {}, and config value is {}",
                            prefix + field.getName(), object);
                }
            }
        }
    }

    @SuppressWarnings("all")
    public static void properties2Object(Properties properties, Object object, String prefix) {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            String mn = method.getName();
            if (mn.startsWith("set")) {
                try {
                    String tmp = mn.substring(4);
                    String first = mn.substring(3, 4);
                    String key = prefix + first.toLowerCase() + tmp;

                    String property = properties.getProperty(key);
                    if (property != null) {
                        Class<?>[] pt = method.getParameterTypes();
                        if (pt.length > 0) {
                            String cn = pt[0].getSimpleName();
                            Object arg = parseProperties(cn, property);
                            if (arg != null) {
                                method.invoke(object, arg);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("setting config value error", e);
                }
            }
        }
    }

    public static void properties2Object(Properties properties, Object object) {
        properties2Object(properties, object, "");
    }

    private static Object parseProperties(String cn, String property) {
        switch (cn) {
            case "int", "Integer" -> {
                return Integer.parseInt(property);
            }
            case "long", "Long" -> {
                return Long.parseLong(property);
            }
            case "double", "Double" -> {
                return Double.parseDouble(property);
            }
            case "boolean", "Boolean" -> {
                return Boolean.parseBoolean(property);
            }
            case "float", "Float" -> {
                return Float.parseFloat(property);
            }
            case "String" -> {
                return property;
            }
            default -> {
                return null;
            }
        }
    }
}
