package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class ShortClassSerializer implements IClassSerializer<Short> {
    public byte getCode() {
        return CODE_SHORT;
    }

    public void write(ObjectOutput objectOutput, Short obj) throws IOException {
        objectOutput.writeShort(obj.shortValue());
    }

    public Short read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return Short.valueOf(objectInput.readShort());
    }
}
