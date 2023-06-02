package com.moon.portal.common.utils;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
public class AssertUtil {

    private AssertUtil(){}

    public static void nonNull(String string, String message) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void nonNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
