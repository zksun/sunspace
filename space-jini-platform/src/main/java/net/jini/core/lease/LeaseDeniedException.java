package net.jini.core.lease;

/**
 * Created by zksun on 25/01/2017.
 */
public class LeaseDeniedException extends LeaseException {
    public LeaseDeniedException() {
    }

    public LeaseDeniedException(String reason) {
        super(reason);
    }
}
