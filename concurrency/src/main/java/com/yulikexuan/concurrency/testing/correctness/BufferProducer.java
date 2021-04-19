//: com.yulikexuan.concurrency.testing.correctness.BufferProducer.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.NonNull;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


final class BufferProducer implements Runnable {

    private final SeedFactory seedFactory = SeedFactory.newSeedFactory();

    private final int nTrials;
    private final CyclicBarrier barrier;
    private final SemaphoreBoundedBuffer<Integer> boundedBuffer;
    private final AtomicInteger putSum;

    private BufferProducer(SemaphoreBoundedBuffer<Integer> boundedBuffer,
                           CyclicBarrier barrier,
                           int nTrials,
                           AtomicInteger putSum) {

        this.boundedBuffer = boundedBuffer;
        this.barrier = barrier;
        this.nTrials = nTrials;
        this.putSum = putSum;
    }

    static BufferProducer of(
            @NonNull SemaphoreBoundedBuffer<Integer> boundedBuffer,
            @NonNull CyclicBarrier barrier,
            int nTrials,
            AtomicInteger putSum) {

        if (nTrials < 1) {
            throw new IllegalArgumentException();
        }

        return new BufferProducer(boundedBuffer, barrier, nTrials, putSum);
    }

    @Override
    public void run() {
        try {
            int sum = 0;
            this.barrier.await();
            int seed = this.seedFactory.initSeed(this);
            for (int i = nTrials; i > 0; --i) {
                this.boundedBuffer.put(seed);
                sum += seed;
                seed = this.seedFactory.nextSeed(seed);
            }
            this.putSum.getAndAdd(sum);
            this.barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}///:~