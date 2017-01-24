package net.jini.core.lookup;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zksun on 24/01/2017.
 */
public final class ServiceID implements Serializable {
    private final long mostSig;
    private final long leastSig;

    public ServiceID(long mostSig, long leastSig) {
        this.mostSig = mostSig;
        this.leastSig = leastSig;
    }

    public ServiceID(DataInput in) throws IOException {
        this.mostSig = in.readLong();
        this.leastSig = in.readLong();
    }

    public long getMostSignificantBits() {
        return this.mostSig;
    }

    public long getLeastSignificantBits() {
        return this.leastSig;
    }

    public void writeBytes(DataOutput out) throws IOException {
        out.writeLong(this.mostSig);
        out.writeLong(this.leastSig);
    }

    @Override
    public int hashCode() {
        return (int) (this.mostSig >> 32 ^ this.mostSig ^ this.leastSig >> 32 ^ this.leastSig);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceID)) {
            return false;
        } else {
            ServiceID serviceID = (ServiceID) obj;
            return this.mostSig == serviceID.mostSig && this.leastSig == serviceID.leastSig;
        }
    }

    @Override
    public String toString() {
        return digits(this.mostSig >> 32, 8) + "-" +
                digits(this.mostSig >> 16, 4) + "-" +
                digits(this.mostSig, 4) + "-" +
                digits(this.leastSig >> 48, 4) + "-" +
                digits(this.leastSig, 12);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << digits * 4;
        return Long.toHexString(hi | val & hi - 1L).substring(1);
    }
}
