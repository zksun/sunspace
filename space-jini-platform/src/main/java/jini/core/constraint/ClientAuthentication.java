package jini.core.constraint;

import java.io.Serializable;

/**
 * Created by zksun on 23/01/2017.
 */
public final class ClientAuthentication implements InvocationConstraint, Serializable {

    public static final ClientAuthentication YES = new ClientAuthentication(true);
    public static final ClientAuthentication NO = new ClientAuthentication(false);
    private final boolean val;

    private ClientAuthentication(boolean val) {
        this.val = val;
    }

    private Object readResolve() {
        return this.val ? YES : NO;
    }

    @Override
    public String toString() {
        return this.val ? "ClientAuthentication.YES" : "ClientAuthentication.NO";
    }
}
