package net.jini.core.lease;

/**
 * Created by zksun on 25/01/2017.
 */
public class LeaseException extends Exception {
    public LeaseException() {
    }

    public LeaseException(String reason) {
        super(reason);
    }

}
