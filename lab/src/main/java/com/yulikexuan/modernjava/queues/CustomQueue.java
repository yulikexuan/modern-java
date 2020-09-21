//: com.yulikexuan.modernjava.queues.CustomQueue.java


package com.yulikexuan.modernjava.queues;


import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;


public class CustomQueue<T> extends AbstractQueue<T> {

    private final LinkedList<T> elements = new LinkedList<>();

    private CustomQueue() {}

    static CustomQueue of() {
        return new CustomQueue();
    }

    @Override
    public Iterator<T> iterator() {
        return this.elements.iterator();
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public boolean offer(T t) {

        if (t == null) {
            return false;
        }

        return this.elements.add(t);
    }

    @Override
    public T poll() {

        Iterator<T> iter = this.elements.iterator();

        T t = iter.next();

        if (t != null) {
            iter.remove();
            return t;
        }

        return null;
    }

    @Override
    public T peek() {
        return this.elements.getFirst();
    }

}///:~