//: com.yulikexuan.concurrency.threadpool.BoundedExecutor.java

package com.yulikexuan.concurrency.threadpool;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * BoundedExecutor
 * <p/>
 * Using a Semaphore to throttle task submission
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
@AllArgsConstructor(staticName = "of")
public class BoundedExecutor {

    private final ExecutorService executor;
    private final Semaphore semaphore;

    public void submitTask(final Runnable command) throws InterruptedException {

        this.semaphore.acquire();

        try {

            this.executor.execute(() -> {
                try {
                    command.run();
                } finally {
                    semaphore.release();
                }
            });

        } catch (RejectedExecutionException ree) {
            semaphore.release();
        }
    }

}///:~