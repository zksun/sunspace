package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class NullClassSerializer<T> implements IClassSerializer<T> {

    public byte getCode() {
        return CODE_NULL;
    }

    public void write(ObjectOutput objectOutput, T obj) throws IOException {

    }

    public T read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return null;
    }
}
