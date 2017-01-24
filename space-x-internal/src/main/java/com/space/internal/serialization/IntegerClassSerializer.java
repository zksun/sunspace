package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class IntegerClassSerializer implements IClassSerializer<Integer> {
    public byte getCode() {
        return CODE_INTEGER;
    }

    public void write(ObjectOutput objectOutput, Integer obj) throws IOException {
        objectOutput.write(obj.intValue());
    }

    public Integer read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return Integer.valueOf(objectInput.readInt());
    }
}
