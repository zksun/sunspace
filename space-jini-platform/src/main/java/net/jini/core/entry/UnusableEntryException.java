package net.jini.core.entry;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by zksun on 24/01/2017.
 */
public class UnusableEntryException extends Exception {

    public Entry partialEntry;
    public String[] unusableFields;
    public Throwable[] nestedExceptions;

    public UnusableEntryException(Entry partial, String[] badFields, Throwable[] exceptions) {
        if (null == partial) {
            if (exceptions.length != 1) {
                throw new IllegalArgumentException("If partial is null exceptions must have one element");
            }
            if (null != badFields) {
                throw new IllegalArgumentException("If partial is null badFields must be null");
            }
        } else if (badFields.length != exceptions.length) {
            throw new IllegalArgumentException("If partial is non-null badFields and exceptions must the same length");
        }

        if (null != badFields) {
            for (int i = 0; i < badFields.length; i++) {
                if (null == badFields[i]) {
                    throw new NullPointerException("badFields has a null element");
                }
            }
        }
        for (int i = 0; i < exceptions.length; i++) {
            if (null == exceptions[i]) {
                throw new NullPointerException("exceptions has a null element");
            }
        }

        this.partialEntry = partial;
        this.unusableFields = badFields;
        this.nestedExceptions = exceptions;

    }

    public UnusableEntryException(Throwable e) {
        if (null == e) {
            throw new NullPointerException("e must be non-null");
        } else {
            this.partialEntry = null;
            this.unusableFields = null;
            this.nestedExceptions = new Throwable[]{e};
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (null == this.partialEntry) {
            if (this.nestedExceptions.length != 1) {
                throw new InvalidObjectException("If partialEntry is null nestedExceptions must have one element");
            }

            if (null == this.unusableFields) {
                throw new InvalidObjectException("If partialEntry is null unusableFields must be null");
            }
        } else {
            if (null == this.unusableFields) {
                throw new InvalidObjectException("unusableFields is null");
            }
            if (null == this.nestedExceptions) {
                throw new InvalidObjectException("nestedExceptions is null");
            }
            if (this.unusableFields.length != this.nestedExceptions.length) {
                throw new InvalidObjectException("If partialEntry is non-null unusableFields and nestedExceptions must have same length");
            }

            if (null != this.unusableFields) {

                for (int i = 0; i < this.unusableFields.length; i++) {
                    if (null == this.unusableFields[i]) {
                        throw new InvalidObjectException("unusableFields has a null element");
                    }
                }
            }

            for (int i = 0; i < this.nestedExceptions.length; i++) {
                if (null == this.nestedExceptions[i]) {
                    throw new InvalidObjectException("nestedExceptions has a null element");
                }
            }
        }
    }

    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("UnusableEntryExceptions should always have data");
    }

    public void printStackTrace() {
        this.printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream stream) {
        synchronized (stream) {
            super.printStackTrace(stream);
            if (null == this.unusableFields) {
                stream.println("Total unmarshalling failure, cause was:");
                this.nestedExceptions[0].printStackTrace(stream);
            } else {
                stream.println("Partial unmarshalling failure");

                for (int i = 0; i < this.nestedExceptions.length; i++) {
                    stream.println(this.unusableFields[i] + " field cound not be unmarshalled because of:");
                    this.nestedExceptions[i].printStackTrace(stream);
                }
            }
        }
    }

    public void printStackTrace(PrintWriter printWriter) {
        synchronized (printWriter) {
            super.printStackTrace(printWriter);
            if (null == this.unusableFields) {
                printWriter.println("Total unmarshalling failure, cause was:");
                this.nestedExceptions[0].printStackTrace(printWriter);
            } else {
                printWriter.println("Partial unmarshalling failure");

                for (int i = 0; i < this.nestedExceptions.length; i++) {
                    printWriter.println(this.unusableFields[i] + " field cound not be unmarshalled because of:");
                    this.nestedExceptions[i].printStackTrace(printWriter);
                }
            }
        }
    }

}
