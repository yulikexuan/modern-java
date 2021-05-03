//: com.yulikexuan.concurrency.state.BaseBoundedBuffer.java

package com.yulikexuan.concurrency.state;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;


/**
 * BaseBoundedBuffer
 * <p/>
 * Base class for bounded buffer implementations
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BaseBoundedBuffer<V> {

    @GuardedBy("this")
    private final V[] buf;

    @GuardedBy("this")
    private int tail;

    @GuardedBy("this")
    private int head;

    @GuardedBy("this")
    private int count;

    protected BaseBoundedBuffer(int capacity) {

        if (capacity < 1) {
            throw new IllegalArgumentException();
        }

        this.buf = (V[]) new Object[capacity];
    }

    protected synchronized final void doPut(V v) {

        buf[tail] = v;

        if (++tail == buf.length) {
            tail = 0;
        }

        ++count;
    }

    protected synchronized final V doTake() {

        V v = buf[head];
        buf[head] = null;

        if (++head == buf.length) {
            head = 0;
        }

        --count;

        return v;
    }

    public synchronized final boolean isFull() {
        return count == buf.length;
    }

    public synchronized final boolean isEmpty() {
        return count == 0;
    }

}///:~