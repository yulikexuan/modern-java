//: com.yulikexuan.concurrency.state.ConditionBoundedBuffer.java

package com.yulikexuan.concurrency.state;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * ConditionBoundedBuffer
 * <p/>
 * Bounded buffer using explicit condition variables
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ConditionBoundedBuffer<T> {

    private static final int BUFFER_SIZE = 100;

    protected final Lock lock = new ReentrantLock();

    // CONDITION PREDICATE: notFull (count < items.length)
    private final Condition notFull = lock.newCondition();

    // CONDITION PREDICATE: notEmpty (count > 0)
    private final Condition notEmpty = lock.newCondition();

    @GuardedBy("lock")
    private final T[] items = (T[]) new Object[BUFFER_SIZE];

    @GuardedBy("lock")
    private int tail, head, count;

    // BLOCKS-UNTIL: notFull
    public void put(T x) throws InterruptedException {

        this.lock.lock();

        try {

            while (this.count == this.items.length) {
                notFull.await();
            }

            this.items[this.tail] = x;

            if (++this.tail == this.items.length) {
                this.tail = 0;
            }

            ++this.count;

            this.notEmpty.signal();

        } finally {
            lock.unlock();
        }
    }

    // BLOCKS-UNTIL: notEmpty
    public T take() throws InterruptedException {

        this.lock.lock();

        try {

            while (this.count == 0) {
                this.notEmpty.await();
            }

            T x = this.items[this.head];

            this.items[this.head] = null;

            if (++this.head == this.items.length) {
                head = 0;
            }

            --this.count;

            this.notFull.signal();

            return x;

        } finally {
            this.lock.unlock();
        }
    }

}///:~