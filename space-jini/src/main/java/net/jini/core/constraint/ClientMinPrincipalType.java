package net.jini.core.constraint;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by zksun on 23/01/2017.
 */
public final class ClientMinPrincipalType implements InvocationConstraint, Serializable {

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("classes", Class[].class, true)
    };

    private final Class[] classes;

    public ClientMinPrincipalType(Class clazz) {
        Constraint.verify(clazz);
        classes = new Class[]{clazz};
    }

    public ClientMinPrincipalType(Class[] classes) {
        this.classes = Constraint.reduce(classes, false);
    }

    public ClientMinPrincipalType(Collection c) {
        classes = Constraint.reduce(c, false);
    }

    @Override
    public int hashCode() {
        return (ClientMinPrincipalType.class.hashCode() + Constraint.hash(classes));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ClientMinPrincipalType && Constraint.equal(classes, ((ClientMinPrincipalType) obj).classes));
    }

    @Override
    public String toString() {
        return "ClientMinPrincipalType" + Constraint.toString(classes);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Constraint.verify(classes);
    }
}
