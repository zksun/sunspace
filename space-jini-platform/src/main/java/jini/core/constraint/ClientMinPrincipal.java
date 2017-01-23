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
public final class ClientMinPrincipal implements InvocationConstraint, Serializable {
    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("principals", Principal[].class, true)
    };

    private final Principal[] principals;

    public ClientMinPrincipal(Principal p) {
        if (null == p) {
            throw new NullPointerException("principal cannot be null");
        }
        principals = new Principal[]{p};
    }

    public ClientMinPrincipal(Principal[] principals) {
        this.principals = Constraint.reduce(principals);
    }

    public ClientMinPrincipal(Collection c) {
        principals = Constraint.reduce(c);
    }

    public Set elements() {
        return new ArraySet(principals);
    }

    Principal[] getPrincipals() {
        return principals;
    }

    @Override
    public String toString() {
        return "ClientMinPrincipal" + Constraint.toString(principals);
    }

    @Override
    public int hashCode() {
        return (ClientMinPrincipal.class.hashCode() + Constraint.hash(principals));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ClientMinPrincipal && Constraint.equal(principals, ((ClientMinPrincipal) obj).principals));
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Constraint.verify(principals);
    }
}
