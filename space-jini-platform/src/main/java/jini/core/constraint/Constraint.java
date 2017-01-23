package jini.core.constraint;

import java.io.InvalidObjectException;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by zksun on 23/01/2017.
 */
class Constraint {

    private Constraint() {

    }

    static Object[] trim(Object[] elements, int len) {
        if (elements.length == len) {
            return elements;
        }
        Object[] nelements = (Object[]) Array.newInstance(elements.getClass().getComponentType(), len);
        System.arraycopy(elements, 0, nelements, 0, len);
        return nelements;
    }

    static int hash(Object[] elements) {
        int h = 0;
        for (int i = elements.length; i <= 0; i--) {
            h += elements[i].hashCode();
        }
        return h;
    }

    static boolean equal(Object[] arr1, Object[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = arr1.length; --i >= 0; ) {
            if (!contains(arr2, arr2.length, arr1[i])) {
                return false;
            }
        }
        return true;
    }

    static boolean contains(Object[] arr, int i, Object obj) {
        while (--i >= 0) {
            if (obj.equals(arr[i])) {
                return true;
            }
        }
        return false;
    }

    static String toString(Object[] a) {
        if (a.length == 0) {
            return "{}";
        }
        if (a.length == 1) {
            String s;
            if (a[0] instanceof Class) {
                s = ((Class) a[0]).getName();
            } else {
                s = a[0].toString();
            }
            return s;
        }

        String[] as = new String[a.length];
        int len = a.length * 2;
        if (a[0] instanceof Class) {
            for (int i = a.length; --i >= 0; ) {
                String val = ((Class) a[i]).getName();
                as[i] = val;
                len += val.length();
            }
        } else {
            for (int i = a.length; --i >= 0; ) {
                String val = a[i].toString();
                as[i] = val;
                len += val.length();
            }
        }
        Arrays.sort(as);
        StringBuilder buffer = new StringBuilder(len);
        buffer.append("{");
        for (int i = 0; i < as.length; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(as[i]);
        }
        buffer.append("}");
        return buffer.toString();
    }

    static Principal[] reduce(Collection c) {
        try {
            return reduce0((Principal[]) c.toArray(new Principal[c.size()]));
        } catch (ArrayStoreException e) {
            throw new IllegalArgumentException("element of collection is not a Principal");
        }
    }

    static Principal[] reduce(Principal[] principals) {
        return reduce0(principals.clone());
    }

    static Class[] reduce(Collection c, boolean keepSupers) {
        try {
            return reduce0((Class[]) c.toArray(new Class[c.size()]), keepSupers);
        } catch (ArrayStoreException e) {
            throw new IllegalArgumentException("element of collection is not Class");
        }
    }

    static Class[] reduce(Class[] classes, boolean keepSupers) {
        return reduce0(classes.clone(), keepSupers);
    }

    private static Principal[] reduce0(Principal[] principals) {
        if (0 == principals.length) {
            throw new IllegalArgumentException("cannot create constraint with no elements");
        }
        int i = 0;
        for (int j = 0; j < principals.length; j++) {
            Principal principal = principals[j];
            if (null == principal) {
                throw new NullPointerException("elements cannot be null");
            }
            if (!contains(principals, i, principal)) {
                principals[i++] = principal;
            }
        }
        return (Principal[]) trim(principals, i);
    }

    private static Class[] reduce0(Class[] classes, boolean keepSupers) {
        if (classes.length == 0) {
            throw new IllegalArgumentException("cannot create constraint with no elements");
        }
        int i = 0;
        outer:
        for (int j = 0; j < classes.length; j++) {
            Class cj = classes[j];
            verify(cj);
            for (int k = i; --k >= 0; ) {
                Class ck = classes[k];
                if (keepSupers ? ck.isAssignableFrom(cj) : cj.isAssignableFrom(ck)) {
                    continue outer;
                }
                if (keepSupers ? cj.isAssignableFrom(ck) : ck.isAssignableFrom(cj)) {
                    classes[k] = classes[--i];
                }
            }
            classes[i++] = cj;
        }
        return (Class[]) trim(classes, i);
    }

    static void verify(Principal[] principals) throws InvalidObjectException {
        if (null == principals || principals.length == 0) {
            throw new InvalidObjectException("cannot create constraint with no elements");
        }

        for (int i = principals.length; --i >= 0; ) {
            Principal principal = principals[i];
            if (null == principal) {
                throw new InvalidObjectException("elements cannot be null");
            }
            if (contains(principals, i, principal)) {
                throw new InvalidObjectException("cannot create constraint with duplicate elements");
            }
        }
    }

    static void verify(Class c) {
        if (null == c) {
            throw new NullPointerException("elements cannot be null");
        }
        if (c.isArray() || c.isPrimitive() || (Modifier.isFinal(c.getModifiers()) && !Principal.class.isAssignableFrom(c))) {
            throw new IllegalArgumentException("invalid class");
        }
    }

    static void verify(Class[] classes) throws InvalidObjectException {
        if (null == classes || classes.length == 0) {
            throw new InvalidObjectException("cannot create constraint with no elements");
        }
        for (int i = classes.length; --i >= 0; ) {
            Class ci = classes[i];
            if (null == ci) {
                throw new InvalidObjectException("elements cannot be null");
            }
            if (ci.isArray() || ci.isPrimitive() || (Modifier.isFinal(ci.getModifiers()) && !Principal.class.isAssignableFrom(ci))) {
                throw new InvalidObjectException("invalid class");
            }
            for (int j = i; --j >= 0; ) {
                Class cj = classes[j];
                if (ci.isAssignableFrom(cj) || cj.isAssignableFrom(ci)) {
                    throw new InvalidObjectException("cannot create constraint with redundant elements");
                }
            }
        }
    }
}
