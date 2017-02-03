package com.s_space.kernel;

import com.space.lrmi.classloading.LRMIClassLoadersHolder;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.rmi.server.RMIClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zksun on 25/01/2017.
 */
public class ClassLoaderHelper {

    private static final Logger _logger = Logger.getLogger("com.space.kernel");
    private static final Map<String, Class<?>> _primitiveTypes = loadPrimitiveTypes();
    private static final Field _directContextClassLoaderThreadField = getContextThreadCLField();
    private static final String NULL_CL_LOGNAME = "null";

    public ClassLoaderHelper() {
    }

    private static final Field getContextThreadCLField() {
        Field threadCLField = null;

        try {
            Thread th = Thread.currentThread();
            threadCLField = Thread.class.getDeclaredField("contextClassLoader");
            threadCLField.setAccessible(true);
            ClassLoader cl = (ClassLoader) threadCLField.get(th);
            threadCLField.set(th, cl);
        } catch (Throwable e) {
            if (_logger.isLoggable(Level.FINEST)) {
                _logger.log(Level.FINEST, "Failed to get access to \'contextClassLoader\' field.", e);
            }
        }

        return threadCLField;
    }

    private static Map<String, Class<?>> loadPrimitiveTypes() {
        Map<String, Class<?>> types = new HashMap();
        types.put(Byte.TYPE.getName(), Byte.TYPE);
        types.put(Short.TYPE.getName(), Short.TYPE);
        types.put(Integer.TYPE.getName(), Integer.TYPE);
        types.put(Long.TYPE.getName(), Long.TYPE);
        types.put(Float.TYPE.getName(), Float.TYPE);
        types.put(Double.TYPE.getName(), Double.TYPE);
        types.put(Boolean.TYPE.getName(), Boolean.TYPE);
        types.put(Character.TYPE.getName(), Character.TYPE);
        return types;
    }

    public static <T> T newInstance(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class cls = loadClass(className);
        return (T) cls.newInstance();
    }

    public static Class loadClass(String className) throws ClassNotFoundException {
        return loadClass(className, false);
    }

    public static Class loadLocalClass(String className) throws ClassNotFoundException {
        return loadClass(className, true);
    }

    public static Class loadClass(String className, boolean localOnly) throws ClassNotFoundException {
        Class primitiveType = (Class) _primitiveTypes.get(className);
        if (primitiveType != null) {
            return primitiveType;
        } else {
            ClassLoader loader = getContextClassLoader();
            if (loader == null) {
                loader = ClassLoaderHelper.class.getClassLoader();
            }

            try {
                Class ex = Class.forName(className, true, loader);
                if (_logger.isLoggable(Level.FINEST)) {
                    _logger.log(Level.FINEST, "Load class: [" + className + "] Thread: [" + Thread.currentThread().getName() + "] using ClassLoader: [" + loader + "]\n" + JSpaceUtilities.getStackTrace(new Exception("Debugging stack trace: ")));
                } else if (_logger.isLoggable(Level.FINE)) {
                    StringBuilder classLoaderHierarchy = new StringBuilder("ClassLoader Hierarchy: ");

                    for (ClassLoader classLoaders = loader; classLoaders != null; classLoaders = classLoaders.getParent()) {
                        classLoaderHierarchy.append(classLoaders.getClass().toString()).append(" <-- ");
                    }

                    _logger.log(Level.FINE, "Load class: [" + className + "] Thread: [" + Thread.currentThread().getName() + "] using ClassLoader: [" + loader + "] \n" + " [ " + classLoaderHierarchy.toString() + " ] \n");
                }

                return ex;
            } catch (ClassNotFoundException var7) {
                if (localOnly) {
                    throw var7;
                } else {
                    if (_logger.isLoggable(Level.FINEST)) {
                        _logger.log(Level.FINEST, "Thread: [" + Thread.currentThread().getName() + "] failed to load class [" + className + "] by Thread ContextClassLoader: [" + loader + "]. Attempting to load by Class.forName()", var7);
                    }

                    return LRMIClassLoadersHolder.loadClass(className);
                }
            }
        }
    }

    public static Class loadClass(String className, boolean localOnly, Class defaultClass) {
        if (null == className) {
            return defaultClass;
        } else {
            try {
                return loadClass(className, localOnly);
            } catch (ClassNotFoundException var4) {
                return defaultClass;
            }
        }
    }

    public static Class loadClass(String codebase, String className, ClassLoader classLoader) throws ClassNotFoundException, MalformedURLException {
        return loadClass(codebase, className, classLoader, false);
    }

    public static Class loadClass(String codebase, String className, ClassLoader classLoader, boolean localOnly) throws ClassNotFoundException, MalformedURLException {
        try {
            return loadClass(className, localOnly);
        } catch (ClassNotFoundException e) {
            if (localOnly) {
                throw e;
            } else {
                try {
                    Class aClass = RMIClassLoader.loadClass(codebase, className, classLoader);
                    if (_logger.isLoggable(Level.FINEST)) {
                        _logger.log(Level.FINEST, "Load class: [" + className + "] Thread: [" + Thread.currentThread().getName() + "] using RMIClassLoader passing codebase: [" + codebase + "] and additional ClassLoader: [" + classLoader + "] \n");
                    }

                    return aClass;
                } catch (ClassNotFoundException ex) {
                    try {
                        return LRMIClassLoadersHolder.loadClass(className);
                    } catch (ClassNotFoundException exception) {
                        if (_logger.isLoggable(Level.FINEST)) {
                            _logger.log(Level.FINEST, "Thread: [" + Thread.currentThread().getName() + "] failed to load class [" + className + "] using LRMIClassLoader \n", exception);
                        }

                        throw exception;
                    }
                }
            }
        }
    }

    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

//    public static void setContextClassLoader(ClassLoader cl, boolean ignoreSecurity) {
//        if (ignoreSecurity) {
//            setDirectContextClassLoader(cl);
//        } else {
//            Thread.currentThread().setContextClassLoader(cl);
//        }
//    }

//    private static void setDirectContextClassLoader(ClassLoader cl) {
//        Thread t = Thread.currentThread();
//        if (t instanceof FastContextClassLoaderThread) {
//            t.setContextClassLoader(cl);
//        } else {
//            if (_directContextClassLoaderThreadField != null) {
//                if (_logger.isLoggable(Level.FINE)) {
//                    _logger.fine("Thread: " + Thread.currentThread() + " set direct contextClassLoader: " + cl);
//                }
//
//                try {
//                    _directContextClassLoaderThreadField.set(Thread.currentThread(), cl);
//                } catch (Throwable var3) {
//                    throw new IllegalArgumentException("Unexpected behavior of Thread.class. Failed to setContextClassLoader.", var3);
//                }
//            } else {
//                Thread.currentThread().setContextClassLoader(cl);
//            }
//        }
//    }

//    public static String getClassLoaderLogName(ClassLoader cl) {
//        return null == cl ? NULL_CL_LOGNAME : (cl instanceof LoggableClassLoader ? ((LoggableClassLoader) cl).getLogName() : cl.toString());
//    }
}
