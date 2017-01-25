package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class ByteClassSerializer implements IClassSerializer<Byte> {

    public byte getCode() {
        return CODE_BYTE;
    }

    public void write(ObjectOutput objectOutput, Byte obj) throws IOException {
        objectOutput.write(obj.byteValue());
    }

    public Byte read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return Byte.valueOf(objectInput.readByte());
    }
}
