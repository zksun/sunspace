package com.space.internal.utils;

import sun.jvm.hotspot.utilities.Assert;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zksun on 25/01/2017.
 */
public abstract class ClassUtils {
    public static final String ARRAY_SUFFIX = "[]";
    private static final char PACKAGE_SEPARATOR_CHAR = '.';
    private static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    private static final String CGLIB_CLASS_SEPARATOR_CHAR = "$$";
    private static final Map<Class, Class> primitiveWrapperTypeMap = new HashMap(8);
    private static final Map<String, Class> primitiveTypeNameMap = new HashMap(8);

    public ClassUtils() {
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (null == contextClassLoader) {
            contextClassLoader = ClassUtils.class.getClassLoader();
        }
        return contextClassLoader;
    }

    public static boolean isPresent(String className) {
        try {
            forName(className);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static Class forName(String name) throws ClassNotFoundException {
        return forName(name, getDefaultClassLoader());
    }

    public static Class forName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        Class clazz = resolvePrimitiveClassName(name);
        if (null != clazz) {
            return clazz;
        } else if (name.endsWith("[]")) {
            String elementClassName = name.substring(0, name.length() - "[]".length());
            Class elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        } else {
            return Class.forName(name, true, classLoader);
        }
    }

    public static Class resolvePrimitiveClassName(String name) {
        if (null != name && name.length() <= 8) {
            return primitiveTypeNameMap.get(name);
        }
        return null;
    }

    public static String getShortName(String className){
        return null;
    }

    static {
        primitiveWrapperTypeMap.put(Boolean.class, Boolean.TYPE);
        primitiveWrapperTypeMap.put(Byte.class, Byte.TYPE);
        primitiveWrapperTypeMap.put(Character.class, Character.TYPE);
        primitiveWrapperTypeMap.put(Double.class, Character.TYPE);
        primitiveWrapperTypeMap.put(Float.class, Character.TYPE);
        primitiveWrapperTypeMap.put(Integer.class, Integer.TYPE);
        primitiveWrapperTypeMap.put(Long.class, Long.TYPE);
        primitiveWrapperTypeMap.put(Short.class, Short.TYPE);

        Iterator<Class> iterator = primitiveWrapperTypeMap.values().iterator();
        while (iterator.hasNext()) {
            Class primitiveClass = iterator.next();
            primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
        }
    }
}
