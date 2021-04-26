//: com.yulikexuan.concurrency.explicitlocks.InterruptibleLocking.java

package com.yulikexuan.concurrency.explicitlocks;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class InterruptibleLocking {

    private Lock lock = new ReentrantLock();

    public boolean sendOnSharedLine(String message) throws InterruptedException {

        this.lock.lockInterruptibly();

        try {
            return cancellableSendOnSharedLine(message);
        } finally {
            this.lock.unlock();
        }
    }

    private boolean cancellableSendOnSharedLine(String message)
            throws InterruptedException {

        /* send something */
        return true;
    }

}///:~