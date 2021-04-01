//: com.yulikexuan.concurrency.util.CallableTask.java

package com.yulikexuan.concurrency.util;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


@Slf4j
@AllArgsConstructor(staticName = "of")
public class CallableTask<T> implements Callable<T> {

    private final Duration runningTime;
    private final T result;

    @Override
    public T call() {
        String threadName = Thread.currentThread().getName();
        try {
            log.info(">>>>>>> Callable Thread {} is running.", threadName);
            TimeUnit.MILLISECONDS.sleep(runningTime.toMillis());
            log.info(">>>>>>> Callable Thread {} has been finished.", threadName);
        } catch (InterruptedException e) {
            log.warn(">>>>>>> Callable Thread {} was just interrupted by {}",
                    threadName, e.getClass().getSimpleName());
            Thread.currentThread().interrupt();
        } finally {
            return this.result;
        }
    }

}///:~