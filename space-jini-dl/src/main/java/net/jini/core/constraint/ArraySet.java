package net.jini.core.constraint;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by zksun on 23/01/2017.
 */
public final class ArraySet implements Set {

    private final Object[] elements;


    ArraySet(Object[] elements) {
        this.elements = elements;
    }

    public int size() {
        return this.elements.length;
    }

    public boolean isEmpty() {
        return this.elements.length == 0;
    }

    public boolean contains(Object o) {
        for (int i = this.elements.length; --i >= 0; ) {
            if (this.elements[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    public Iterator iterator() {
        return new Iter();
    }

    public Object[] toArray() {
        Object[] a = new Object[this.elements.length];
        System.arraycopy(this.elements, 0, a, 0, this.elements.length);
        return a;
    }

    public Object[] toArray(Object[] a) {
        if (a.length < this.elements.length) {
            a = (Object[]) Array.newInstance(a.getClass().getComponentType(), this.elements.length);
        }
        System.arraycopy(this.elements, 0, a, 0, this.elements.length);
        if (a.length > this.elements.length) {
            a[this.elements.length] = null;
        }
        return a;
    }

    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection c) {
        return false;
    }

    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection c) {
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            if (!contains(iter.next())) {
                return false;
            }
        }
        return true;
    }

    private final class Iter implements Iterator {
        private int idx = 0;

        Iter() {
        }

        public boolean hasNext() {
            return this.idx < ArraySet.this.elements.length;
        }


        public Object next() {
            if (this.idx < elements.length) {
                return elements[idx++];
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
