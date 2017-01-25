package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class FloatClassSerializer implements IClassSerializer<Float> {
    public byte getCode() {
        return CODE_FLOAT;
    }

    public void write(ObjectOutput objectOutput, Float obj) throws IOException {
        objectOutput.writeFloat(obj.floatValue());
    }

    public Float read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return Float.valueOf(objectInput.readFloat());
    }
}
