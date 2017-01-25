package com.space.internal.io;

import java.io.IOException;

/**
 * Created by zksun on 24/01/2017.
 */
public class IOArrayException extends IOException {
    private static final long serialVersionUID = 1L;
    private int _index;

    public IOArrayException() {
    }

    public IOArrayException(int index) {
        this._index = index;
    }

    public IOArrayException(int index, String message) {
        super(message);
        this._index = index;
    }

    public IOArrayException(int index, String message, IOException cause) {
        this(index, message);
        this.initCause(cause);
    }

    public int getIndex() {
        return _index;
    }
}
