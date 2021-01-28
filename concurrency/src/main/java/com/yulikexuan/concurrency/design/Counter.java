//: com.yulikexuan.concurrency.design.Counter.java

package com.yulikexuan.concurrency.design;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;


@ThreadSafe
public class Counter {

    @GuardedBy("this")
    private long value = 0;

    public synchronized long getValue() {
        return value;
    }

    public synchronized long increment() {

        if (value == Long.MAX_VALUE) {
            throw new IllegalStateException("Counter Overflow");
        }

        return ++value;
    }

}///:~