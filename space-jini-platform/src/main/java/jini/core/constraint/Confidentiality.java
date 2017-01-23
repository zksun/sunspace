package jini.core.constraint;

import java.io.Serializable;

/**
 * Created by zksun on 23/01/2017.
 */
public final class Confidentiality implements InvocationConstraint, Serializable {

    public static final Confidentiality YES = new Confidentiality(true);
    public static final Confidentiality NO = new Confidentiality(false);

    private final boolean val;

    private Confidentiality(boolean val) {
        this.val = val;
    }

    public String toString() {
        return val ? "Confidentiality.YES" : "Confidentiality.NO";
    }

    private Object readResolve() {
        return val ? YES : NO;
    }
}
