package net.jini.core.constraint;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;

/**
 * Created by hanshou on 23/01/2017.
 */
public final class ClientMaxPrincipal implements InvocationConstraint, Serializable {

    private final Principal[] principals;

    public ClientMaxPrincipal(Principal principal) {
        if (null == principal) {
            throw new NullPointerException("principal cannot be null");
        }
        this.principals = new Principal[]{principal};
    }

    public ClientMaxPrincipal(Principal[] principal) {
        this.principals = new Principal[]{principal};
    }

    private void readObject() throws IOException, ClassNotFoundException {

    }
}
