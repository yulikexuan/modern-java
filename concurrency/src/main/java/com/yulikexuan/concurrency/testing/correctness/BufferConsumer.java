//: com.yulikexuan.concurrency.testing.correctness.BufferConsumer.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.NonNull;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


final class BufferConsumer implements Runnable {

    private final int nTrials;
    private final CyclicBarrier barrier;
    private final SemaphoreBoundedBuffer<Integer> boundedBuffer;
    private final AtomicInteger takeSum;

    private BufferConsumer(SemaphoreBoundedBuffer<Integer> boundedBuffer,
                           CyclicBarrier barrier, int nTrials,
                           AtomicInteger takeSum) {

        this.boundedBuffer = boundedBuffer;
        this.barrier = barrier;
        this.nTrials = nTrials;
        this.takeSum = takeSum;
    }

    static BufferConsumer of(
            @NonNull SemaphoreBoundedBuffer<Integer> boundedBuffer,
            @NonNull CyclicBarrier barrier, int nTrials,
            @NonNull AtomicInteger takeSum) {

        if (nTrials < 1) {
            throw new IllegalArgumentException();
        }

        return new BufferConsumer(boundedBuffer, barrier, nTrials, takeSum);
    }

    @Override
    public void run() {
        try {
            this.barrier.await();
            int sum = 0;
            for (int i = this.nTrials; i > 0; --i) {
                sum += this.boundedBuffer.take();
            }
            this.takeSum.getAndAdd(sum);
            this.barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}///:~