package net.jini.core.event;

/**
 * Created by zksun on 25/01/2017.
 */
public class UnknownEventException extends Exception {
    public UnknownEventException(String reason) {
        super(reason);
    }

    public UnknownEventException() {
        super();
    }
}
