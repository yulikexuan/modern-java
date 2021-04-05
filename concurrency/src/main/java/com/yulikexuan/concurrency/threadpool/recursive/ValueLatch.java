//: com.yulikexuan.concurrency.threadpool.recursive.ValueLatch.java

package com.yulikexuan.concurrency.threadpool.recursive;


import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.CountDownLatch;


/**
 * ValueLatch
 * <p/>
 * Result-bearing latch used by ConcurrentPuzzleSolver
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ValueLatch<T> {

    @GuardedBy("this")
    private T value = null;

    private final CountDownLatch doneLatch = new CountDownLatch(1);

    public boolean isSet() {
        return (this.doneLatch.getCount() == 0);
    }

    public synchronized void setValue(T newValue) {
        if (!this.isSet()) {
            this.value = newValue;
            this.doneLatch.countDown();
        }
    }

    public T getValue() throws InterruptedException {
        this.doneLatch.await();
        synchronized (this) {
            return value;
        }
    }

}///:~