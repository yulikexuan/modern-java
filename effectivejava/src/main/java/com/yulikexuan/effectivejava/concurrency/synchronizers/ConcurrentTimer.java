//: com.yulikexuan.effectivejava.concurrency.synchronizers.ConcurrentTimer.java

package com.yulikexuan.effectivejava.concurrency.synchronizers;


import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;


/*
 * Simple framework for timing concurrent execution 327
 *
 * CountDownLatch: A synchronization aid that allows one or more threads to
 * wait until a set of operations being performed in other threads completes
 *
 * A CountDownLatch is initialized with a given count
 * The await methods block until the current count reaches zero due to
 * invocations of the countDown() method after which all waiting threads are
 * released and any subsequent invocations of await return immediately
 *
 */
public class ConcurrentTimer {

    private ConcurrentTimer() {} // Noninstantiable

    public static long time(Executor executor, int concurrency, Runnable action)
            throws InterruptedException {

        StopWatch stopWatch = StopWatch.create();

        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            executor.execute(() -> {
                ready.countDown(); // Tell timer we're ready
                try {
                    start.await(); // Wait till peers are ready
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();  // Tell timer we're done
                }
            });
        }

        ready.await(); // Wait for all workers to be ready

        stopWatch.start();

        start.countDown(); // And they're off!

        done.await(); // Wait for all workers to finish

        stopWatch.stop();

        return stopWatch.getNanoTime();
    }

}///:~