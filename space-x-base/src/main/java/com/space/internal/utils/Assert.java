package com.space.internal.utils;

/**
 * Created by zksun on 25/01/2017.
 */
public class Assert {
    public Assert() {
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isNull(Object object, String message) {
        if (null != object) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object){
        isNull(object,"[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message){
        if(object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it cannot be null");
    }

    public static void hasLength(String text, String message) {
        if(!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(message);
        }
    }
}
