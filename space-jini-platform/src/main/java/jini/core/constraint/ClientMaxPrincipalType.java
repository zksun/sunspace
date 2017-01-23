package jini.core.constraint;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Created by zksun on 23/01/2017.
 */
public final class ClientMaxPrincipalType implements InvocationConstraint, Serializable {

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("classes", Class[].class, true)
    };

    private final Class[] classes;

    public ClientMaxPrincipalType(Class clazz) {
        Constraint.verify(clazz);
        classes = new Class[]{clazz};
    }

    public ClientMaxPrincipalType(Class[] classes) {
        this.classes = Constraint.reduce(classes, true);
    }

    public ClientMaxPrincipalType(Collection c) {
        classes = Constraint.reduce(c, true);
    }

    public Set elements() {
        return new ArraySet(classes);
    }

    @Override
    public int hashCode() {
        return (ClientMaxPrincipalType.class.hashCode() + Constraint.hash(classes));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ClientMaxPrincipalType && Constraint.equal(classes, ((ClientMaxPrincipalType) obj).classes));
    }

    @Override
    public String toString() {
        return "ClientMaxPrincipalType" + Constraint.toString(classes);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Constraint.verify(classes);
    }
}
