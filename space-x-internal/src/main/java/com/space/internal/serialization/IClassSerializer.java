package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Created by zksun on 24/01/2017.
 */
public interface IClassSerializer<T> {
    byte CODE_NULL = 0;
    byte CODE_OBJECT = -1;
    byte CODE_STRING = -2;
    byte CODE_BYTE = -11;
    byte CODE_SHORT = -12;
    byte CODE_INTEGER = -13;
    byte CODE_LONG = -14;
    byte CODE_FLOAT = -15;
    byte CODE_DOUBLE = -16;
    byte CODE_BOOLEAN = -17;
    byte CODE_CHARACTER = -18;
    byte CODE_BYTE_ARRAY = -21;
    byte CODE_HASHMAP = 10;

    byte getCode();

    void write(ObjectOutput objectOutput, T obj) throws IOException;

    T read(ObjectInput objectInput) throws IOException, ClassNotFoundException;

}
