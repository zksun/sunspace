package jini.core.constraint;

import java.io.Serializable;

/**
 * Created by zksun on 23/01/2017.
 */
public final class ConnectionAbsoluteTime implements InvocationConstraint, Serializable {

    private final long time;

    public ConnectionAbsoluteTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    @Override
    public int hashCode() {
        return (int) (ConnectionAbsoluteTime.class.hashCode() + time);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConnectionAbsoluteTime)) {
            return false;
        }
        ConnectionAbsoluteTime cc = (ConnectionAbsoluteTime) obj;
        return time == cc.time;
    }

    @Override
    public String toString() {
        return "ConnectionAbsoluteTime[" + time + "]";
    }
}
