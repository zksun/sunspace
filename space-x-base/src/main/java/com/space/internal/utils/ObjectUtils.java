package com.space.internal.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zksun on 25/01/2017.
 */
public abstract class ObjectUtils {
    private static final int INITIAL_HASH = 7;
    private static final int MULTIPLIER = 31;
    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = "null";
    private static final String ARRAY_START = "{";
    private static final String ARRAY_END = "}";
    private static final String EMPTY_ARRAY = "{}";
    private static final String ARRAY_ELEMENT_SEPARATOR = ", ";
    private static final Map<String, Class<?>> _primitiveTypes = initPrimitiveTypes();
    private static final Map<Class<?>, Object> _defaultValues = initDefaultValues();

    public ObjectUtils() {
    }

    private static Map<String, Class<?>> initPrimitiveTypes() {
        Map<String, Class<?>> result = new HashMap();
        result.put(Byte.TYPE.getName(), Byte.TYPE);
        result.put(Short.TYPE.getName(), Short.TYPE);
        result.put(Integer.TYPE.getName(), Integer.TYPE);
        result.put(Long.TYPE.getName(), Long.TYPE);
        result.put(Float.TYPE.getName(), Float.TYPE);
        result.put(Double.TYPE.getName(), Double.TYPE);
        result.put(Boolean.TYPE.getName(), Boolean.TYPE);
        result.put(Character.TYPE.getName(), Character.TYPE);
        return result;
    }

    private static Map<Class<?>, Object> initDefaultValues() {
        Map<Class<?>, Object> result = new HashMap();
        result.put(Byte.TYPE, Byte.valueOf((byte) 0));
        result.put(Short.TYPE, Short.valueOf((short) 0));
        result.put(Integer.TYPE, Integer.valueOf(0));
        result.put(Long.TYPE, Long.valueOf(0L));
        result.put(Float.TYPE, Float.valueOf(0.0F));
        result.put(Double.TYPE, Double.valueOf(0.0D));
        result.put(Boolean.TYPE, Boolean.valueOf(false));
        result.put(Character.TYPE, Character.valueOf('\u0000'));
        return result;
    }

    public static boolean isCheckedException(Throwable ex) {
        return !(ex instanceof RuntimeException) && !(ex instanceof Error);
    }

    public static boolean isCompatibleWithThrowsClause(Throwable ex, Class<?>[] declaredExceptions) {
        if (!isCheckedException(ex)) {
            return true;
        } else {
            if (null != declaredExceptions) {
                for (int i = 0; i < declaredExceptions.length; i++) {
                    Class declaredException = declaredExceptions[i];
                    if (declaredException.isAssignableFrom(ex.getClass())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static <T> T[] addObjectToArray(T[] array, T obj) {
        Class compType;
        if (null != array) {
            compType = array.getClass().getComponentType();
        } else if (null != obj) {
            compType = obj.getClass();
        } else {
            compType = Object.class;
        }

        int newArrLength = null != array ? array.length + 1 : 1;
        Object[] newArr = (Object[]) Array.newInstance(compType, newArrLength);
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }

        newArr[newArr.length - 1] = obj;
        return (T[]) newArr;
    }

    public static int hashCode(Object o) {
        return null == o ? 0 : o.hashCode();
    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (null != o1 && null != o2) {
            return o1.equals(o2);
        }
        return false;
    }

    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (null == o1 || null == o2) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if ((o1 instanceof Object[]) && (o2 instanceof Object[])) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if ((o1 instanceof byte[]) && (o2 instanceof byte[])) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if ((o1 instanceof short[]) && (o2 instanceof short[])) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        if ((o1 instanceof int[]) && (o2 instanceof int[])) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if ((o1 instanceof long[]) && (o2 instanceof long[])) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if ((o1 instanceof float[]) && (o2 instanceof float[])) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if ((o1 instanceof double[]) && (o2 instanceof double[])) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if ((o1 instanceof boolean[]) && (o2 instanceof boolean[])) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if ((o1 instanceof char[]) && (o2 instanceof char[])) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        return false;
    }

    public static int nullSafeHashCode(Object obj) {
        if (null == obj) {
            return 0;
        }
        if (obj instanceof Object[]) {
            return nullSafeHashCode((Object[]) obj);
        }
        if (obj instanceof boolean[]) {
            return nullSafeHashCode((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeHashCode((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return nullSafeHashCode((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeHashCode((double[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeHashCode((float[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeHashCode((int[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeHashCode((long[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeHashCode((short[]) obj);
        }
        return obj.hashCode();
    }

    public static int nullSafeHashCode(boolean[] array) {
        if (null == array) {
            return 0;
        }
        int hash = INITIAL_HASH;
        int arraySize = array.length;
        for (int i = 0; i < arraySize; i++) {
            hash = MULTIPLIER * hash + hashCode(array[i]);
        }
        return hash;
    }

    public static int nullSafeHashCode(byte[] array) {
        if (null == array) {
            return 0;
        }
        int hash = INITIAL_HASH;
        int arraySize = array.length;
        for (int i = 0; i < arraySize; i++) {
            hash = MULTIPLIER * hash + hashCode(array[i]);
        }
        return hash;
    }

    public static int nullSafeHashCode(char[] array) {
        if (null == array) {
            return 0;
        }
        int hash = INITIAL_HASH;
        int arraySize = array.length;
        for (int i = 0; i < arraySize; i++) {
            hash = MULTIPLIER * hash + hashCode(array[i]);
        }
        return hash;
    }

    public static int nullSafeHashCode(double[] array) {
        if (null == array) {
            return 0;
        }
        int hash = INITIAL_HASH;
        int arraySize = array.length;
        for (int i = 0; i < arraySize; i++) {
            hash = MULTIPLIER * hash + hashCode(array[i]);
        }
        return hash;
    }

    public static int nullSafeHashCode(float[] array) {
        if (null == array) {
            return 0;
        }
        int hash = INITIAL_HASH;
        int arraySize = array.length;
        for (int i = 0; i < arraySize; i++) {
            hash = MULTIPLIER * hash + hashCode(array[i]);
        }
        return hash;
    }

    public static int nullSafeHashCode(int[] array) {
        if (null == array) {
            return 0;
        }
        int hash = INITIAL_HASH;
        int arraySize = array.length;
        for (int i = 0; i < arraySize; i++) {
            hash = MULTIPLIER * hash + hashCode(array[i]);
        }
        return hash;
    }

    public static int nullSafeHashCode(long[] array) {
        if (null == array) {
            return 0;
        }
        int hash = INITIAL_HASH;
        int arraySize = array.length;
        for (int i = 0; i < arraySize; i++) {
            hash = MULTIPLIER * hash + hashCode(array[i]);
        }
        return hash;
    }

    public static int nullSafeHashCode(short[] array) {
        if (null == array) {
            return 0;
        }
        int hash = INITIAL_HASH;
        int arraySize = array.length;
        for (int i = 0; i < arraySize; i++) {
            hash = MULTIPLIER * hash + hashCode(array[i]);
        }
        return hash;
    }

    public static int hashCode(boolean bool) {
        return bool ? 1231 : 1237;
    }

    public static int hashCode(double dbl) {
        long bits = Double.doubleToLongBits(dbl);
        return hashCode(bits);
    }

    public static int hashCode(float flt) {
        return Float.floatToIntBits(flt);
    }

    public static int hashCode(long lng) {
        return (int) (lng ^ lng >>> 32);
    }

    public static String nullSafeToString(Object obj) {
        if (null == obj) {
            return NULL_STRING;
        }

        if (obj instanceof String) {
            return (String) obj;
        }

        if (obj instanceof Object[]) {
            return nullSafeToString((Object[]) obj);
        }

        if (obj instanceof boolean[]) {
            return nullSafeToString((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeToString((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((double[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeToString((float[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeToString((int[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeToString((long[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeToString((short[]) obj);
        }
        return obj.toString();
    }

    public static String nullSafeToString(Object[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String nullSafeToString(boolean[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String nullSafeToString(byte[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String nullSafeToString(char[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String nullSafeToString(double[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String nullSafeToString(float[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String nullSafeToString(int[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String nullSafeToString(long[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String nullSafeToString(short[] array) {
        if (null == array) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                buffer.append(ARRAY_START);
            } else {
                buffer.append(ARRAY_ELEMENT_SEPARATOR);
            }
            buffer.append(String.valueOf(array[i]));
        }
        buffer.append(ARRAY_END);
        return buffer.toString();
    }

    public static String identityToString(Object obj) {
        if (null == obj) {
            return EMPTY_STRING;
        }
        return obj.getClass().getName() + "@" + getIdentityHexString(obj);
    }

    public static String getIdentityHexString(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    public static String getDisplayString(Object obj) {
        return obj == null ? EMPTY_STRING : nullSafeToString(obj);
    }

    public static boolean isPrimitive(String typeName) {
        return _primitiveTypes.containsKey(typeName);
    }

    public static Class<?> getPrimitive(String typeName) {
        return (Class) _primitiveTypes.get(typeName);
    }

    public static Object getDefaultValue(Class<?> type) {
        return _defaultValues.get(type);
    }

    public static <T> T assertArgumentNotNull(T argument, String argumentName) {
        if (null == argument) {
            throw new IllegalArgumentException("Argument cannot be null - \'" + argumentName + "\'");
        } else {
            return argument;
        }
    }

}
