package net.jini.core.constraint;

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
public final class ServerMinPrincipal implements InvocationConstraint, Serializable {

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("principals", Principal.class, true)
    };

    private final Principal[] principals;


    public ServerMinPrincipal(Principal principal) {
        if (null == principal) {
            throw new NullPointerException("principal cannot be null");
        }
        principals = new Principal[]{principal};
    }

    public ServerMinPrincipal(Principal[] principals) {
        this.principals = Constraint.reduce(principals);
    }

    public ServerMinPrincipal(Collection collection) {
        principals = Constraint.reduce(collection);
    }

    public Set elements() {
        return new ArraySet(principals);
    }

    Principal[] getPrincipals() {
        return principals;
    }

    @Override
    public int hashCode() {
        return (ServerMinPrincipal.class.hashCode() + Constraint.hash(principals));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ServerMinPrincipal && Constraint.equal(principals, ((ServerMinPrincipal) obj).principals));
    }

    @Override
    public String toString() {
        return "ServerMinPrincipal" + Constraint.toString(principals);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Constraint.verify(principals);
    }
}
