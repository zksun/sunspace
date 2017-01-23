package jini.core.constraint;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by zksun on 23/01/2017.
 */
public final class InvocationConstraints implements Serializable {

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("reqs", InvocationConstraint[].class, true),
            new ObjectStreamField("prefs", InvocationConstraint[].class, true)
    };

    private static final InvocationConstraint[] empty = new InvocationConstraint[0];

    private static final int REL_REQS = 1;
    private static final int REL_PREFS = 2;

    public static final InvocationConstraints EMPTY = new InvocationConstraints((InvocationConstraint) null, null);

    private InvocationConstraint[] reqs;

    private InvocationConstraint[] prefs;

    private transient int rel = 0;

    public InvocationConstraints(InvocationConstraint req,
                                 InvocationConstraint pref) {
        if (null != req) {
            this.reqs = new InvocationConstraint[]{req};
        }
        if (null != pref) {
            this.prefs = new InvocationConstraint[]{pref};
        }
        reduce();
    }

    private InvocationConstraints(InvocationConstraint[] reqs, int reqidx, InvocationConstraint[] prefs, int prefix, int rel) {
        this.reqs = reqs;
        this.prefs = prefs;
        reduce(reqidx, prefix);
        this.rel = rel;
    }


    private void reduce() {
        if (null == reqs) {
            reqs = empty;
        }
        if (null == prefs) {
            prefs = empty;
        }
        reduce(0, 0);
        setRelative(reqs, REL_REQS);
        setRelative(reqs, REL_PREFS);
    }

    private void reduce(int reqidx, int prefidx) {
        for (int i = reqidx; i < reqs.length; i++) {
            InvocationConstraint req = reqs[i];
            if (null == req) {
                throw new NullPointerException("elements cannot be null");
            } else if (!Constraint.contains(reqs, reqidx, req)) {
                reqs[reqidx++] = req;
            }
        }
        reqs = (InvocationConstraint[]) Constraint.trim(reqs, reqidx);
        for (int i = prefidx; i < prefs.length; i++) {
            InvocationConstraint pref = prefs[i];
            if (null == pref) {
                throw new NullPointerException("elements cannot be null");
            } else if (!Constraint.contains(prefs, prefidx, pref) && !Constraint.contains(reqs, reqs.length, pref)) {
                prefs[prefidx++] = pref;
            }
        }
        prefs = (InvocationConstraint[]) Constraint.trim(prefs, prefidx);
    }

    private static boolean relative(InvocationConstraint constraint) {
        return (constraint instanceof RelativeTimeConstraint &&
                (!(constraint instanceof ConstraintAlternatives) ||
                        ((ConstraintAlternatives) constraint).relative()));
    }

    private void setRelative(InvocationConstraint[] constraints, int flag) {
        for (int i = constraints.length; --i >= 0; ) {
            if (relative(constraints[i])) {
                rel |= flag;
                return;
            }
        }
    }

    public static InvocationConstraints combine(InvocationConstraints constraints1, InvocationConstraints constraints2) {
        if (null == constraints1 || constraints1.isEmpty()) {
            return null == constraints2 ? EMPTY : constraints2;
        } else if (null == constraints2 || constraints2.isEmpty()) {
            return constraints1;
        } else if (constraints2.reqs.length > constraints1.reqs.length) {
            InvocationConstraints tmp = constraints1;
            constraints1 = constraints2;
            constraints2 = tmp;
        }

        int prefidx;
        InvocationConstraint[] reqs;
        if (constraints2.reqs.length > 0) {
            reqs = concat(constraints1.reqs, constraints2.reqs);
            prefidx = 0;
        } else {
            reqs = constraints1.reqs;
            prefidx = constraints1.prefs.length;
        }
        InvocationConstraint[] prefs;
        if (constraints1.prefs.length > 0 || constraints2.prefs.length > 0) {
            prefs = concat(constraints1.prefs, constraints2.prefs);
        } else {
            prefs = empty;
        }

        return new InvocationConstraints(reqs, constraints1.reqs.length, prefs, prefidx, constraints1.rel | constraints2.rel);
    }

    private static InvocationConstraint[] concat(InvocationConstraint[] arr1, InvocationConstraint[] arr2) {
        InvocationConstraint[] res = new InvocationConstraint[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, res, 0, arr1.length);
        System.arraycopy(arr2, 0, res, arr1.length, arr2.length);
        return res;
    }

    private static InvocationConstraint[] makeAbsolute(InvocationConstraint[] arr, long baseTime) {
        InvocationConstraint[] narr = new InvocationConstraint[arr.length];
        for (int i = arr.length; --i >= 0; ) {
            InvocationConstraint constraint = arr[i];
            if (constraint instanceof RelativeTimeConstraint) {
                constraint = ((RelativeTimeConstraint) constraint).makeAbsolute(baseTime);
            }
            narr[i] = constraint;
        }
        return narr;
    }

    public InvocationConstraints makeAbsolute(long baseTime) {
        if (rel == 0) {
            return this;
        }
        InvocationConstraint[] nreqs;
        int reqidx;
        if ((rel & REL_REQS) != 0) {
            nreqs = makeAbsolute(reqs, baseTime);
            reqidx = 0;
        } else {
            nreqs = reqs;
            reqidx = reqs.length;
        }
        InvocationConstraint[] nprefs;
        if ((rel & REL_PREFS) != 0) {
            nprefs = makeAbsolute(prefs, baseTime);
        } else {
            nprefs = prefs.clone();
        }
        return new InvocationConstraints(nreqs, reqidx, nprefs, 0, 0);
    }

    public InvocationConstraints makeAbsolute() {
        if (rel == 0) {
            return this;
        }
        return makeAbsolute(System.currentTimeMillis());
    }

    public Set requirements() {
        return new ArraySet(reqs);
    }

    public Set preferences() {
        return new ArraySet(prefs);
    }


    public boolean isEmpty() {
        return reqs.length == 0 && prefs.length == 0;
    }

    @Override
    public int hashCode() {
        return Constraint.hash(reqs) + Constraint.hash(prefs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof InvocationConstraints)) {
            return false;
        }
        InvocationConstraints sc = (InvocationConstraints) obj;
        return (Constraint.equal(reqs, sc.reqs) && Constraint.equal(prefs, sc.prefs));
    }

    @Override
    public String toString() {
        return ("InvocationConstraints[reqs: " + Constraint.toString(reqs) +
                ", prefs: " + Constraint.toString(prefs) + "]");
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        verify(reqs);
        verify(prefs);
        for (int i = prefs.length; --i >= 0; ) {
            if (Constraint.contains(reqs, reqs.length, prefs[i])) {
                throw new InvalidObjectException("cannot create constraint with redundant elements");
            }
        }

        setRelative(reqs, REL_REQS);
        setRelative(prefs, REL_PREFS);

    }

    private static void verify(InvocationConstraint[] constraints) throws InvalidObjectException {
        if (null == constraints) {
            throw new InvalidObjectException("array cannot be null");
        }
        for (int i = constraints.length; --i >= 0; ) {
            InvocationConstraint constraint = constraints[i];
            if (null == constraint) {
                throw new InvalidObjectException("elements cannot be null");
            } else if (Constraint.contains(constraints, i, constraint)) {
                throw new InvalidObjectException("cannot create constraint with redundant elements");
            }

        }
    }
}
