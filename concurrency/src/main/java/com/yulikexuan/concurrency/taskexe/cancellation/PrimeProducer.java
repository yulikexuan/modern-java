//: com.yulikexuan.concurrency.taskexe.cancellation.PrimeProducer.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * PrimeProducer
 * <p/>
 * Using interruption for cancellation
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
public class PrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    private volatile boolean cancelled = false;

    private PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public static PrimeProducer of(BlockingQueue<BigInteger> primeQueue) {
        return new PrimeProducer(primeQueue);
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!isPrimeProducerInterrupted()) {
                queue.put(p = p.nextProbablePrime());
            }
        } catch (InterruptedException consumed) {
            log.warn(">>>>>>> The Prime Producer was Interrupted.");
        } finally {
            this.cancelled = true;
        }
    }

    private boolean isPrimeProducerInterrupted() {
        boolean interrupted = Thread.currentThread().isInterrupted();
        if (interrupted) {
            log.warn(">>>>>>> The interruption status is true now");
        }
        return interrupted;
    }

    public void cancel() {
        interrupt();
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

}///:~