//: com.yulikexuan.concurrency.state.ThreadGate.java

package com.yulikexuan.concurrency.state;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

/**
 * ThreadGate
 * <p/>
 * Recloseable gate using wait and notifyAll
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ThreadGate {

    // CONDITION-PREDICATE: opened-since(n) (isOpen || generation > n)
    @GuardedBy("this")
    private boolean isOpen;

    @GuardedBy("this")
    private int generation;

    public synchronized void close() {
        this.isOpen = false;
    }

    public synchronized void open() {
        ++this.generation;
        this.isOpen = true;
        notifyAll();
    }

    // BLOCKS-UNTIL: opened-since(generation on entry)
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = this.generation;
        while (!this.isOpen && (arrivalGeneration == this.generation)) {
            wait();
        }
    }

}///:~