//: com.yulikexuan.concurrency.unblocking.CasCounter.java

package com.yulikexuan.concurrency.atomics;


import javax.annotation.concurrent.ThreadSafe;


/**
 * CasCounter
 * <p/>
 * Nonblocking counter using CAS
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class CasCounter {

    private SimulatedCAS value;

    public int getValue() {
        return this.value.get();
    }

    public int increment() {

        int v;

        do {
            v = this.value.get();
        } while (v != value.compareAndSwap(v, v + 1));

        return v + 1;
    }

}///:~