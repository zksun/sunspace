package jini.core.constraint;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zksun on 23/01/2017.
 */
public final class DelegationAbsoluteTime implements InvocationConstraint, Serializable {
    private final long minStart;
    private final long maxStart;
    private final long minStop;
    private final long maxStop;


    private static SoftReference formatterRef;

    public DelegationAbsoluteTime(long minStart, long maxStart, long minStop, long maxStop) {
        if (minStart > maxStart || maxStart > minStop || minStop > maxStop) {
            throw new IllegalArgumentException("illegal times");
        }
        this.minStart = minStart;
        this.maxStart = maxStart;
        this.minStop = minStop;
        this.maxStop = maxStop;
    }

    public DelegationAbsoluteTime(Date minStart,
                                  Date maxStart,
                                  Date minStop,
                                  Date maxStop) {
        this(minStart.getTime(), maxStart.getTime(), minStop.getTime(), maxStop.getTime());
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

    @Override
    public int hashCode() {
        return (int) (DelegationAbsoluteTime.class.hashCode() + minStart + maxStart + minStop + maxStop);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DelegationAbsoluteTime)) {
            return false;
        }
        DelegationAbsoluteTime dc = (DelegationAbsoluteTime) obj;
        return (minStart == dc.minStart && maxStart == dc.maxStart && minStop == dc.minStop && maxStop == dc.maxStop);
    }

    @Override
    public String toString() {
        SimpleDateFormat format = getFormatter();
        FieldPosition position = new FieldPosition(0);
        StringBuffer buf = new StringBuffer(95);
        buf.append("DelegationAbsoluteTime[start: ");
        format(minStart, maxStart, format, buf, position);
        buf.append(", stop: ");
        format(minStop, maxStop, format, buf, position);
        buf.append(']');
        return buf.toString();
    }

    private static void format(long min, long max, SimpleDateFormat format, StringBuffer buf, FieldPosition position) {
        if (min == max) {
            format.format(new Date(min), buf, position);
        } else {
            buf.append('[');
            format.format(new Date(min), buf, position);
            buf.append(", ");
            format.format(new Date(max), buf, position);
            buf.append(']');
        }
    }


    private static synchronized SimpleDateFormat getFormatter() {
        SimpleDateFormat format = null;
        if (null != formatterRef) {
            format = (SimpleDateFormat) formatterRef.get();
        }
        if (null == formatterRef) {
            format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSSS zzz", Locale.US);
            formatterRef = new SoftReference(format);
        }
        return format;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (minStart > maxStart || maxStart > minStop || minStop > maxStop) {
            throw new InvalidObjectException("ivalid times");
        }
    }
}
