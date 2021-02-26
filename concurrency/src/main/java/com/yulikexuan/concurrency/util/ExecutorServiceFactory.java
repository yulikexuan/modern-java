//: com.yulikexuan.concurrency.util.ExecutorServiceFactory.java

package com.yulikexuan.concurrency.util;


import com.google.common.util.concurrent.MoreExecutors;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;


public class ExecutorServiceFactory {

    public static final int MAXIMUM_NUMBER_OF_THREADS = 127;
    public static final long DEFAULT_KEEP_ALIVE_TIME = 1000L;

    public static ExecutorService createFixedPoolSizeExecutor(
            int numberOfThreads) throws Exception {

        final int numberOfCreatedThreads =
                numberOfThreads > MAXIMUM_NUMBER_OF_THREADS ?
                        MAXIMUM_NUMBER_OF_THREADS : numberOfThreads;

        BlockingQueue<Runnable> workQueue = new LinkedTransferQueue<>();

        ThreadPoolExecutor executor =  new ThreadPoolExecutor(numberOfThreads,
                numberOfThreads, DEFAULT_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                workQueue);
        executor.allowCoreThreadTimeOut(true);

        return MoreExecutors.getExitingExecutorService(executor,
                Duration.ofMillis(DEFAULT_KEEP_ALIVE_TIME));
    }

    public static ScheduledExecutorService newExitingScheduledExecutorService(
            int corePoolSize, Duration terminationTimeout) {

        return MoreExecutors.getExitingScheduledExecutorService(
                new ScheduledThreadPoolExecutor(corePoolSize),
                terminationTimeout.toMillis(),
                TimeUnit.MILLISECONDS);
    }

    public static ExecutorService newExitingExecutorService(
            int corePoolSize, int maximumPoolSize, long keepAliveMillis,
            long terminationTimeoutMillis) {

        BlockingQueue<Runnable> workingQueue = new LinkedTransferQueue<>();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveMillis,
                TimeUnit.MILLISECONDS,
                workingQueue);

        executor.allowCoreThreadTimeOut(true);

        return MoreExecutors.getExitingExecutorService(executor,
                terminationTimeoutMillis, TimeUnit.MILLISECONDS);
    }

    public static Executor newDelayedExecutor(long deferredMillis,
                                              long terminationTimeoutMillis) {

        final Duration serviceTerminationTimeOut = Duration
                .of(terminationTimeoutMillis, ChronoUnit.MILLIS);

        ScheduledExecutorService delayedActionScheduler =
                newExitingScheduledExecutorService(1,
                        serviceTerminationTimeOut);

        return runnable -> delayedActionScheduler.schedule(
                () -> ForkJoinPool.commonPool().execute(runnable),
                deferredMillis, TimeUnit.MILLISECONDS);
    }

}///:~