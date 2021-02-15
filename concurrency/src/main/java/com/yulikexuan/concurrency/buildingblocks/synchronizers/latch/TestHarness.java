//: com.yulikexuan.concurrency.buildingblocks.synchronizers.latch.TestHarness.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers.latch;


import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.CountDownLatch;


/**
 * TestHarness
 * <p/>
 * Using CountDownLatch for starting and stopping threads in timing tests
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TestHarness {

    static long timeTasks(int nThreads, final Runnable task)
            throws InterruptedException {

        StopWatch stopWatch = StopWatch.create();

        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }

        stopWatch.start();

        startGate.countDown();
        endGate.await();

        stopWatch.stop();

        return stopWatch.getNanoTime();
    }

}///:~