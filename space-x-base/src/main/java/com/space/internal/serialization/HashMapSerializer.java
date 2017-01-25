package com.space.internal.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

/**
 * Created by zksun on 24/01/2017.
 */
public class HashMapSerializer implements IClassSerializer<Map<String, Object>> {

    public byte getCode() {
        return CODE_HASHMAP;
    }

    public void write(ObjectOutput objectOutput, Map<String, Object> obj) throws IOException {
        objectOutput.writeObject(obj);
    }

    public Map<String, Object> read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return (Map<String, Object>) objectInput.readObject();
    }
}
