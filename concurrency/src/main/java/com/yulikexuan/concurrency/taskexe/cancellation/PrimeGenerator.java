//: com.yulikexuan.concurrency.taskexe.cancellation.PrimeGenerator.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import com.google.common.collect.Lists;
import lombok.NonNull;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


public
/**
 * PrimeGenerator
 * <p/>
 * Using a volatile field to hold cancellation state
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
class PrimeGenerator implements Runnable {

    private volatile boolean cancelled;

    @GuardedBy("this")
    private final List<BigInteger> primes;

    private PrimeGenerator() {
        this.primes = Lists.newArrayList();
    }

    static PrimeGenerator of() {
        return new PrimeGenerator();
    }

    @Override
    public void run() {

        BigInteger prime = BigInteger.ONE;

        // When the task checks whether cancellation has been requested
        while (!cancelled) {
            synchronized (this) {
                primes.add(prime);
            }
        }

        // What actions the task takes in response to a cancellation request
    }

    // How other code can request cancellation
    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return new ArrayList<BigInteger>(primes);
    }

    static List<BigInteger> aDurationOfPrimes(
            @NonNull ExecutorService executor, @NonNull Duration duration)
            throws InterruptedException {

        PrimeGenerator generator = PrimeGenerator.of();

        executor.execute(generator);

        try {
            Thread.sleep(duration.toMillis());
        } finally {
            generator.cancel();
        }

        return generator.get();
    }

}///:~