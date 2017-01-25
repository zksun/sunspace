package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class DoubleClassSerializer implements IClassSerializer<Double> {
    public byte getCode() {
        return CODE_DOUBLE;
    }

    public void write(ObjectOutput objectOutput, Double obj) throws IOException {
        objectOutput.writeObject(obj.doubleValue());
    }

    public Double read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return Double.valueOf(objectInput.readDouble());
    }
}
