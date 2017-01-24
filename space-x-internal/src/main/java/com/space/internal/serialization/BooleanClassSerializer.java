package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class BooleanClassSerializer implements IClassSerializer<Boolean> {

    public byte getCode() {
        return CODE_BOOLEAN;
    }

    public void write(ObjectOutput objectOutput, Boolean obj) throws IOException {
        objectOutput.writeBoolean(obj.booleanValue());
    }

    public Boolean read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return Boolean.valueOf(objectInput.readBoolean());
    }
}
