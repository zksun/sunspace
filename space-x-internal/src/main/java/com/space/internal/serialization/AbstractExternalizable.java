package com.space.internal.serialization;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by hanshou on 24/01/2017.
 */
public abstract class AbstractExternalizable implements Externalizable {
    private static final long serialVersionUID = 1L;

    public void writeExternal(ObjectOutput out) throws IOException {
        this.writeExternalImpl(out);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.readExternalImpl(in);
    }

    protected abstract void readExternalImpl(ObjectInput in) throws IOException, ClassNotFoundException;

    protected abstract void writeExternalImpl(ObjectOutput out) throws IOException;
}
