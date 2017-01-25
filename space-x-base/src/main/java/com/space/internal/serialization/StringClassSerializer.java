package com.space.internal.serialization;

import com.space.internal.io.IOUtils;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class StringClassSerializer implements IClassSerializer<String> {
    public byte getCode() {
        return CODE_STRING;
    }

    public void write(ObjectOutput objectOutput, String obj) throws IOException {
        IOUtils.writeString(objectOutput, obj);
    }

    public String read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return IOUtils.readString(objectInput);
    }
}
