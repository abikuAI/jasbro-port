package jasbro;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class WeakList<T> extends AbstractList<T> {
   private MutableBoolean copyListLock = new MutableBoolean(false);
   private final ReferenceQueue<T> queue = new ReferenceQueue<>();
   private final List<WeakList.ListEntry<T>> list = new ArrayList<>();

   @Override
   public boolean add(T o) {
      this.expungeStaleEntries();
      return this.list.add(new WeakList.ListEntry<>(o, this.queue));
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

   public List<T> strongCopy() {
      synchronized (this.getCopyListLock()) {
         List<T> copy;
         try {
            this.getCopyListLock().setTrue();
            int size = this.size();
            copy = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
               T element = this.get(i);
               if (element != null) {
                  copy.add(element);
               }
            }
         } finally {
            this.getCopyListLock().setFalse();
         }

         return copy;
      }
   }

   private void expungeStaleEntries() {
      Object r;
      while ((r = this.queue.poll()) != null) {
         int i = this.list.indexOf(r);
         if (i != -1) {
            synchronized (this.getCopyListLock()) {
               if (!this.getCopyListLock().booleanValue()) {
                  this.list.remove(i);
               }
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

   private static class ListEntry<T> extends WeakReference<T> {
      String objectString;

      public ListEntry(T o, ReferenceQueue<T> queue) {
         super(o, queue);
         this.objectString = o.toString();
      }

      @Override
      public boolean equals(Object o) {
         if (o == null) {
            return false;
         } else if (o instanceof WeakList.ListEntry) {
            return o.hashCode() == this.hashCode();
         } else {
            return this.get() == null ? false : this.get().equals(o);
         }
      }

      @Override
      public String toString() {
         return this.get() == null ? "'entry " + this.objectString + " <GARBAGED>'" : "'entry " + this.get() + "'";
      }
   }
}
