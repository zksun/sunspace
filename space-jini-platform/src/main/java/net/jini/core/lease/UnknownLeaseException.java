package net.jini.core.lease;

/**
 * Created by zksun on 25/01/2017.
 */
public class UnknownLeaseException extends LeaseException {
    public UnknownLeaseException() {
    }

    public UnknownLeaseException(String reason) {
        super(reason);
    }

}
