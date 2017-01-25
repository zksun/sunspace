package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public class CharacterClassSerializer implements IClassSerializer<Character> {
    public byte getCode() {
        return CODE_CHARACTER;
    }

    public void write(ObjectOutput objectOutput, Character obj) throws IOException {
        objectOutput.write(obj.charValue());
    }

    public Character read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return Character.valueOf(objectInput.readChar());
    }
}
