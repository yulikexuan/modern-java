//: com.yulikexuan.concurrency.util.ExecutorServiceTerminator.java

package com.yulikexuan.concurrency.util;


import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public interface ExecutorServiceTerminator {

    int TERMINATE_TIMEOUT_MILLISECOND = 3000;

    static void terminateExecutorServeceAfter(
            ExecutorService executorService, Duration duration) throws Exception {

        executorService.awaitTermination(duration.toMillis(), TimeUnit.MILLISECONDS);
        terminateExecutorService(executorService);
    }

    static void terminateExecutorService(ExecutorService executorService) {
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