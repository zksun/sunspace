package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class LongClassSerializer implements IClassSerializer<Long> {
    public byte getCode() {
        return CODE_LONG;
    }

    public void write(ObjectOutput objectOutput, Long obj) throws IOException {
        objectOutput.writeLong(obj.longValue());
    }

    public Long read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return Long.valueOf(objectInput.readLong());
    }
}
