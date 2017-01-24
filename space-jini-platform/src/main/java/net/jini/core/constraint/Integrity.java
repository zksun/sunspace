package net.jini.core.constraint;

import java.io.Serializable;

/**
 * Created by zksun on 23/01/2017.
 */
public final class Integrity implements InvocationConstraint, Serializable {

    public static final Integrity YES = new Integrity(true);

    public static final Integrity NO = new Integrity(false);

    private final boolean val;

    private Integrity(boolean val) {
        this.val = val;
    }

    public String toString() {
        return val ? "Integrity.YES" : "Integrity.NO";
    }

    private Object readResolve() {
        return val ? YES : NO;
    }

}
