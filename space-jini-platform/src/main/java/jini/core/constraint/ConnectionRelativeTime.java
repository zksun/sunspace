package jini.core.constraint;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by hanshou on 23/01/2017.
 */
public final class ConnectionRelativeTime implements RelativeTimeConstraint, Serializable {

    private final long time;

    public ConnectionRelativeTime(long time) {
        if (time < 0) {
            throw new IllegalArgumentException("invalid duration");
        }
        this.time = time;
    }


    public long getTime() {
        return time;
    }

    public InvocationConstraint makeAbsolute(long baseTime) {
        return new ConnectionAbsoluteTime(add(time, baseTime));
    }

    private static long add(long dur, long time) {
        long ntime = time + dur;
        if (ntime < 0 && time > 0) {
            ntime = Long.MAX_VALUE;
        }
        return ntime;
    }

    @Override
    public int hashCode() {
        return (int) (ConnectionRelativeTime.class.hashCode() + time);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConnectionRelativeTime)) {
            return false;
        }
        ConnectionRelativeTime cc = (ConnectionRelativeTime) obj;
        return time == cc.time;
    }

    @Override
    public String toString() {
        return "ConnectionRelativeTime[" + time + "]";
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (time < 0) {
            throw new InvalidObjectException("invalid dration");
        }
    }
}
