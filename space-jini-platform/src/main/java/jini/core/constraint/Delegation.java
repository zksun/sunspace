package jini.core.constraint;

import java.io.Serializable;

/**
 * Created by zksun on 23/01/2017.
 */
public class Delegation implements InvocationConstraint, Serializable {
    private static final Delegation YES = new Delegation(true);
    private static final Delegation NO = new Delegation(false);


    private final boolean val;

    private Delegation(boolean val) {
        this.val = val;
    }

    public String toString() {
        return val ? "Delegation.YES" : "Delegation.NO";
    }

    private Object readResolve() {
        return val ? YES : NO;
    }
}
