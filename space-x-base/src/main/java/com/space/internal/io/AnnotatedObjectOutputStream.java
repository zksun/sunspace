package com.space.internal.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.server.RMIClassLoader;

/**
 * Created by zksun on 25/01/2017.
 */
public class AnnotatedObjectOutputStream extends ObjectOutputStream {

    private static final boolean support_code_base = Boolean.getBoolean("com.space.transport_protocol.lrmi.support-codebase");

    public AnnotatedObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    protected void annotateClass(Class cl) throws IOException {
        this.writeAnnotation(cl);
    }

    protected void annotateProxyClass(Class cl) throws IOException {
        this.writeAnnotation(cl);
    }

    protected void writeAnnotation(Class cl) throws IOException {
        String annotation = support_code_base ? RMIClassLoader.getClassAnnotation(cl) : null;
        this.writeUnshared(annotation);
    }
}
