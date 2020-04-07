//: com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceConfig.java

package com.yulikexuan.modernjava.concurrency.executors;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public interface ExecutorServiceConfig {

    int NUMBER_OF_THREADS = 4;
    int NUMBER_OF_INCREMENTS = 100;
    int SIZE_OF_THREAD_POOL = 8;
    int TERMINATE_TIMEOUT_MILLISECOND = 3000;

    static void terminateExecutorServeceAfter(
            ExecutorService executorService, Duration duration) throws Exception {

        executorService.awaitTermination(duration.toMillis(), TimeUnit.MILLISECONDS);
        terminateExecutorServece(executorService);
    }

    static void terminateExecutorServece(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(
                    TERMINATE_TIMEOUT_MILLISECOND, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

}///:~