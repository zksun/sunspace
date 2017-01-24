package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class ObjectClassSerializer implements IClassSerializer<Object> {
    public byte getCode() {
        return CODE_OBJECT;
    }

    public void write(ObjectOutput objectOutput, Object obj) throws IOException {
        objectOutput.writeObject(obj);
    }

    public Object read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return objectInput.readObject();
    }
}
