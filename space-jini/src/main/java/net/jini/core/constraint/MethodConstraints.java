package net.jini.core.constraint;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Created by zksun on 23/01/2017.
 */
public interface MethodConstraints {
    InvocationConstraints getConstraints(Method method);

    Iterator possibleConstraints();
}
