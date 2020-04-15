//: com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceFactory.java


package com.yulikexuan.modernjava.concurrency.executors;


import com.google.common.util.concurrent.MoreExecutors;

import java.time.Duration;
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

}///:~