/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.mutable.MutableBoolean
 */
package jasbro;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class WeakList<T>
extends AbstractList<T> {
    private MutableBoolean copyListLock = new MutableBoolean(false);
    private final ReferenceQueue<T> queue = new ReferenceQueue();
    private final List<ListEntry<T>> list = new ArrayList<ListEntry<T>>();

    @Override
    public boolean add(T o) {
        this.expungeStaleEntries();
        return this.list.add(new ListEntry<T>(o, this.queue));
    }

    @Override
    public T get(int i) {
        this.expungeStaleEntries();
        return this.list.get(i).get();
    }

    @Override
    public int size() {
        this.expungeStaleEntries();
        return this.list.size();
    }

    @Override
    public T remove(int index) {
        return this.list.remove(index).get();
    }

    @Override
    public void clear() {
        this.list.clear();
        super.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<T> strongCopy() {
        ArrayList<T> copy;
        MutableBoolean mutableBoolean = this.getCopyListLock();
        synchronized (mutableBoolean) {
            try {
                this.getCopyListLock().setTrue();
                int size = this.size();
                copy = new ArrayList<T>(size);
                for (int i = 0; i < size; ++i) {
                    T element = this.get(i);
                    if (element == null) continue;
                    copy.add(element);
                }
            }
            finally {
                this.getCopyListLock().setFalse();
            }
        }
        return copy;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void expungeStaleEntries() {
        Reference<T> r;
        while ((r = this.queue.poll()) != null) {
            int i = this.list.indexOf(r);
            if (i == -1) continue;
            MutableBoolean mutableBoolean = this.getCopyListLock();
            synchronized (mutableBoolean) {
                if (!this.getCopyListLock().booleanValue()) {
                    this.list.remove(i);
                }
            }
        }
    }

    private synchronized MutableBoolean getCopyListLock() {
        if (this.copyListLock == null) {
            this.copyListLock = new MutableBoolean(false);
        }
        return this.copyListLock;
    }

    private static class ListEntry<T>
    extends WeakReference<T> {
        String objectString;

        public ListEntry(T o, ReferenceQueue<T> queue) {
            super(o, queue);
            this.objectString = o.toString();
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof ListEntry) {
                return o.hashCode() == this.hashCode();
            }
            if (this.get() == null) {
                return false;
            }
            return this.get().equals(o);
        }

        public String toString() {
            if (this.get() == null) {
                return "'entry " + this.objectString + " <GARBAGED>'";
            }
            return "'entry " + this.get() + "'";
        }
    }
}

