//: com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceFactory.java


package com.yulikexuan.modernjava.concurrency.executors;


import java.util.concurrent.*;


public class ExecutorServiceFactory {

    public static final int MAXIMUM_NUMBER_OF_THREADS = 64;

    public static ExecutorService createFixedPoolSizeExecutor(
            int numberOfThreads) throws Exception {

        final int numberOfCreatedThreads =
                numberOfThreads > MAXIMUM_NUMBER_OF_THREADS ?
                        MAXIMUM_NUMBER_OF_THREADS : numberOfThreads;

        BlockingQueue<Runnable> workQueue = new LinkedTransferQueue<>();
        ThreadPoolExecutor executor =  new ThreadPoolExecutor(numberOfThreads,
                numberOfThreads,1000, TimeUnit.MILLISECONDS, workQueue);
        executor.allowCoreThreadTimeOut(true);

        return executor;
    }

}///:~