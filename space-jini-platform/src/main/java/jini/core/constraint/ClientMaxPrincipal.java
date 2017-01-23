package jini.core.constraint;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

/**
 * Created by zksun on 23/01/2017.
 */
public final class ClientMaxPrincipal implements InvocationConstraint, Serializable {

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("principals", Principal[].class, true)
    };
    private final Principal[] principals;

    public ClientMaxPrincipal(Principal principal) {
        if (null == principal) {
            throw new NullPointerException("principal cannot be null");
        }
        this.principals = new Principal[]{principal};
    }

    public ClientMaxPrincipal(Principal[] principals) {
        this.principals = Constraint.reduce(principals);
    }

    public ClientMaxPrincipal(Collection c) {
        principals = Constraint.reduce(c);
    }

    public Set elements() {
        return new ArraySet(principals);
    }

    Principal[] getPrincipals() {
        return principals;
    }

    @Override
    public int hashCode() {
        return (ClientMaxPrincipal.class.hashCode() + Constraint.hash(principals));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ClientMaxPrincipal && Constraint.equal(principals, ((ClientMaxPrincipal) obj).principals));
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Constraint.verify(principals);
    }
}
