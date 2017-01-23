package net.jini.core.constraint;

import java.io.Serializable;

/**
 * Created by zksun on 23/01/2017.
 */
public final class ServerAuthentication implements InvocationConstraint, Serializable {

    private static final ServerAuthentication YES = new ServerAuthentication(true);

    private static final ServerAuthentication NO = new ServerAuthentication(false);

    private final boolean val;

    private ServerAuthentication(boolean val) {
        this.val = val;
    }

    public String toString() {
        return val ? "ServerAuthentication.YES" : "ServerAuthentication.NO";
    }

    private Object readResolve() {
        return val ? YES : NO;
    }

}
