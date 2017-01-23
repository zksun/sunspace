package jini.core.constraint;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Created by zksun on 23/01/2017.
 */
public final class ConstraintAlternatives implements RelativeTimeConstraint, Serializable {

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("constraints", InvocationConstraint[].class, true)
    };

    private final InvocationConstraint[] constraints;

    private transient boolean rel = false;

    public ConstraintAlternatives(InvocationConstraint[] constraints) {
        this.constraints = reduce(constraints.clone());
        setRelative();
    }

    public ConstraintAlternatives(Collection c) {
        try {
            constraints = reduce((InvocationConstraint[]) c.toArray(new InvocationConstraint[c.size()]));
        } catch (ArrayStoreException e) {
            throw new IllegalArgumentException("element of collection is not an InvocationConstraint");
        }
        setRelative();
    }

    public static InvocationConstraint create(InvocationConstraint[] constraints) {
        return reduce(constraints.clone(), false);
    }

    public static InvocationConstraint create(Collection c) {
        try {
            return reduce((InvocationConstraint[]) c.toArray(new InvocationConstraint[c.size()]), false);
        } catch (ArrayStoreException e) {
            throw new IllegalArgumentException(
                    "element of collection is not an InvocationConstraint");
        }
    }

    private ConstraintAlternatives(InvocationConstraint[] constraints, boolean allAbs) {
        this.constraints = constraints;
        if (!allAbs) {
            setRelative();
        }
    }

    private void setRelative() {
        for (int i = constraints.length; --i >= 0; ) {
            if (constraints[i] instanceof RelativeTimeConstraint) {
                rel = true;
                return;
            }
        }
    }

    boolean relative() {
        return rel;
    }

    private static InvocationConstraint reduce(InvocationConstraint[] constraints,
                                               boolean allAbs) {
        verify(constraints, 1);
        int n = reduce0(constraints);
        if (n == 1) {
            return constraints[0];
        }
        return new ConstraintAlternatives((InvocationConstraint[]) Constraint.trim(constraints, n), allAbs);
    }

    private static InvocationConstraint[] reduce(InvocationConstraint[] constraints) {
        verify(constraints, 2);
        int n = reduce0(constraints);
        if (n == 1) {
            throw new IllegalArgumentException("reduced to less than 2 elements");
        }
        return (InvocationConstraint[]) Constraint.trim(constraints, n);
    }

    private static int reduce0(InvocationConstraint[] constraints) {
        int i = 0;
        for (int j = 0; j < constraints.length; j++) {
            InvocationConstraint constraint = constraints[j];
            if (!Constraint.contains(constraints, i, constraint)) {
                constraints[i++] = constraint;
            }
        }
        return i;
    }

    private static void verify(InvocationConstraint[] constraints, int min) {
        if (constraints.length < min) {
            throw new IllegalArgumentException("cannot create constraint with " +
                    (min == 1 ? "no" : "less than " + min) +
                    " elements");
        }
        for (int i = constraints.length; --i >= 0; ) {
            InvocationConstraint c = constraints[i];
            if (null == c) {
                throw new NullPointerException("elements cannot be null");
            } else if (c instanceof ConstraintAlternatives) {
                throw new IllegalArgumentException("elements cannot be ConstraintAlternatives instances");
            }
        }
    }

    public Set elements() {
        return new ArraySet(constraints);
    }

    InvocationConstraint[] getConstraints() {
        return constraints;
    }

    public InvocationConstraint makeAbsolute(long baseTime) {
        if (!rel) {
            return this;
        }
        InvocationConstraint[] vals = new InvocationConstraint[constraints.length];
        for (int i = vals.length; --i >= 0; ) {
            InvocationConstraint constraint = constraints[i];
            if (constraint instanceof RelativeTimeConstraint) {
                constraint = ((RelativeTimeConstraint) constraint).makeAbsolute(baseTime);
            }
            vals[i] = constraint;
        }
        return reduce(vals, true);
    }

    @Override
    public int hashCode() {
        return Constraint.hash(constraints);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ConstraintAlternatives && Constraint.equal(constraints, ((ConstraintAlternatives) obj).constraints));
    }

    @Override
    public String toString() {
        return "ConstraintAlternatives" + Constraint.toString(constraints);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (null == constraints) {
            throw new InvalidClassException("cannot create constraint with no elements");
        }

        try {
            verify(constraints, 2);
        } catch (RuntimeException e) {
            if (e instanceof NullPointerException || e instanceof IllegalArgumentException) {
                InvalidObjectException ee = new InvalidObjectException(e.getMessage());
                ee.initCause(e);
                throw ee;
            }
            throw e;
        }
        for (int i = constraints.length; --i >= 0; ) {
            if (Constraint.contains(constraints, i, constraints[i])) {
                throw new InvalidObjectException("cannot create constraint with duplicate elements");
            }
        }
        setRelative();
    }
}
