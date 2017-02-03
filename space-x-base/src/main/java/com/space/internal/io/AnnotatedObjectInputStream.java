package com.space.internal.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Created by zksun on 25/01/2017.
 */
public class AnnotatedObjectInputStream extends ObjectInputStream {
    public AnnotatedObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    protected Class resolveClass(ObjectStreamClass classDesc, boolean readAnnotation, String codebase) throws IOException, ClassNotFoundException {
        if (readAnnotation) {
            codebase = readAnnotation(classDesc);
        }

        String name = classDesc.getName();


        return null;

    }

    protected Class resolveClass(ObjectStreamClass classDesc) throws IOException, ClassNotFoundException {
        return this.resolveClass(classDesc, true, null);
    }

    protected String readAnnotation(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        try {
            return (String) this.readUnshared();
        } catch (ClassCastException e) {
            InvalidObjectException invalidObjectException = new InvalidObjectException("Annotation is not String or null");
            invalidObjectException.initCause(e);
            throw invalidObjectException;
        }
    }
}
