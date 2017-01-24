package com.space.internal.serialization;

import com.space.internal.io.IOUtils;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class ByteArrayClassSerializer implements IClassSerializer<byte[]> {
    public byte getCode() {
        return CODE_BYTE_ARRAY;
    }

    public void write(ObjectOutput objectOutput, byte[] obj) throws IOException {
        IOUtils.writeByteArray(objectOutput, obj);
    }

    public byte[] read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return IOUtils.readByteArray(objectInput);
    }
}
