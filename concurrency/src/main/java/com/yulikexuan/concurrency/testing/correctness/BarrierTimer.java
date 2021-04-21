//: com.yulikexuan.concurrency.testing.correctness.BarrierTimer.java

package com.yulikexuan.concurrency.testing.correctness;

import lombok.NonNull;
import org.checkerframework.checker.index.qual.NonNegative;

public class BarrierTimer implements Runnable {

    private boolean started;
    private long startTime, endTime;

    @Override
    public synchronized void run() {

        long t = System.nanoTime();

        if (!this.started) {
            this.started = true;
            this.startTime = t;
        } else {
            this.endTime = t;
        }
    }

    public synchronized void clear() {
        this.started = false;
    }

    public synchronized long getTime() {
        return this.endTime - this.startTime;
    }

    public long getThroughput(@NonNull long items) {
        if (items == 0) {
            throw new IllegalArgumentException();
        }
        return this.getTime() / items;
    }

}///:~