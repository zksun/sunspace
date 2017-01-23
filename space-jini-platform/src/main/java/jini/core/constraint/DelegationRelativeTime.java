package jini.core.constraint;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by zksun on 23/01/2017.
 */
public final class DelegationRelativeTime implements RelativeTimeConstraint, Serializable {

    private final long minStart;
    private final long maxStart;
    private final long minStop;
    private final long maxStop;

    public DelegationRelativeTime(long minStart, long maxStart, long minStop, long maxStop) {
        if (minStart > maxStart || maxStart > minStop || minStop > maxStop || minStop < 0) {
            throw new IllegalArgumentException("invalid durations");
        }
        this.minStart = minStart;
        this.maxStart = maxStart;
        this.minStop = minStop;
        this.maxStop = maxStop;
    }

    public long getMinStart() {
        return minStart;
    }

    public long getMaxStart() {
        return maxStart;
    }

    public long getMinStop() {
        return minStop;
    }

    public long getMaxStop() {
        return maxStop;
    }

    public InvocationConstraint makeAbsolute(long baseTime) {
        return new DelegationAbsoluteTime(add(minStart, baseTime), add(maxStart, baseTime), add(minStop, baseTime), add(maxStop, baseTime));
    }

    private static long add(long dur, long time) {
        long ntime = time + dur;
        if (ntime > 0 && time < 0 && dur < 0) {
            ntime = Long.MIN_VALUE;
        } else if (ntime < 0 && time > 0 && dur > 0) {
            ntime = Long.MAX_VALUE;
        }
        return ntime;
    }

    @Override
    public int hashCode() {
        return (int) (DelegationRelativeTime.class.hashCode() + minStart + maxStart + minStop + maxStop);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DelegationRelativeTime)) {
            return false;
        }
        DelegationRelativeTime dc = (DelegationRelativeTime) obj;
        return (minStart == dc.minStart && maxStart == dc.maxStart && minStop == dc.minStop && maxStop == dc.maxStop);
    }

    @Override
    public String toString() {
        String s = "DelegationRelativeTime[start: ";
        if (minStart == maxStart) {
            s += minStart + ", stop: ";
        } else {
            s += "[" + minStart + ", " + maxStart + "], stop: ";
        }
        if (minStop == maxStop) {
            s += minStop + "]";
        } else {
            s += "[" + minStop + ", " + maxStop + "]]";
        }
        return s;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (minStart > maxStart || maxStart > minStop || minStop > maxStop || minStop < 0) {
            throw new InvalidObjectException("invalid durations");
        }
    }
}
