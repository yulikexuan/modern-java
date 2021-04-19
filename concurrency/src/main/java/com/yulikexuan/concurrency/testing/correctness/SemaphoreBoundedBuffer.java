//: com.yulikexuan.concurrency.testing.correctness.SemaphoreBoundedBuffer.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.NonNull;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.Semaphore;


/**
 * BoundedBuffer
 * <p/>
 * Bounded buffer using \Semaphore
 *
 * The sum of the counts of both semaphores always equals the bound
 *
 * In practice, if you need a bounded buffer you should use ArrayBlockingQueue
 * or LinkedBlockingQueue rather than rolling your own, but the technique used
 * here illustrates how insertions and removals can be controlled in other data
 * structures as well
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SemaphoreBoundedBuffer<E> {

    @GuardedBy("this")
    private final E[] items;

    @GuardedBy("this")
    private int putPosition = 0;

    @GuardedBy("this")
    private int takePosition = 0;

    /*
     * The availableItems semaphore represents the number of elements that can
     * be removed from the buffer, and is initially zero (since the buffer is
     * initially empty)
     */
    private final Semaphore availableItems;

    /*
     * The availableSpaces represents how many items can be inserted into the
     * buffer, and is initialized to the size of the buffer
     */
    private final Semaphore availableSpaces;

    private SemaphoreBoundedBuffer(@NonNull E[] items,
                                   @NonNull Semaphore availableItems,
                                   @NonNull Semaphore availableSpaces) {
        this.items = items;
        this.availableItems = availableItems;
        this.availableSpaces = availableSpaces;
    }

    public static <E> SemaphoreBoundedBuffer of(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        E[] items = (E[]) new Object[capacity];
        Semaphore availableItems = new Semaphore(0);
        Semaphore availableSpaces = new Semaphore(capacity);

        return new SemaphoreBoundedBuffer(items, availableItems, availableSpaces);
    }

    public boolean isEmpty() {
        return this.availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return this.availableSpaces.availablePermits() == 0;
    }

    public void put(E x) throws InterruptedException {
        this.availableSpaces.acquire();
        this.doInsert(x);
        this.availableItems.release();
    }

    public E take() throws InterruptedException {
        this.availableItems.acquire();
        E item = this.doExtract();
        this.availableSpaces.release();
        return item;
    }

    private synchronized void doInsert(E x) {
        int i = putPosition;
        this.items[i] = x;
        this.putPosition = (++i == items.length) ? 0 : i;
    }

    private synchronized E doExtract() {
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length) ? 0 : i;
        return x;
    }

}///:~