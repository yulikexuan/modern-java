//: com.yulikexuan.concurrency.state.SemaphoreOnLock.java

package com.yulikexuan.concurrency.state;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * SemaphoreOnLock
 * <p/>
 * Counting semaphore implemented using Lock
 * (Not really how java.util.concurrent.Semaphore is implemented)
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SemaphoreOnLock {

    private final Lock lock = new ReentrantLock();

    // CONDITION PREDICATE: permitsAvailable (permits > 0)
    private final Condition permitsAvailable = this.lock.newCondition();

    @GuardedBy("lock")
    private int permits;

    SemaphoreOnLock(int initialPermits) {

        this.lock.lock();

        try {
            this.permits = initialPermits;
        } finally {
            this.lock.unlock();
        }
    }

    // BLOCKS-UNTIL: permitsAvailable
    public void acquire() throws InterruptedException {

        this.lock.lock();

        try {

            while (this.permits <= 0) {
                this.permitsAvailable.await();
            }

            --this.permits;

        } finally {
            this.lock.unlock();
        }
    }

    public void release() {

        this.lock.lock();

        try {
            ++this.permits;
            this.permitsAvailable.signal();
        } finally {
            this.lock.unlock();
        }
    }

}///:~