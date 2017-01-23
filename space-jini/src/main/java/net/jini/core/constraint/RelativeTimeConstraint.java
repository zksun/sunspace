package net.jini.core.constraint;

/**
 * Created by zksun on 23/01/2017.
 */
public interface RelativeTimeConstraint extends InvocationConstraint {
    InvocationConstraint makeAbsolute(long baseTime);
}
