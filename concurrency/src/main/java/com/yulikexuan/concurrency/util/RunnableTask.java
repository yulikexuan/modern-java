//: com.yulikexuan.concurrency.util.RunnableTask.java

package com.yulikexuan.concurrency.util;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Slf4j
@AllArgsConstructor(staticName = "of")
class RunnableTask implements Runnable {

    private final Duration runningTime;

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            log.info(">>>>>>> {} is running.", threadName);
            TimeUnit.MILLISECONDS.sleep(runningTime.toMillis());
            log.info(">>>>>>> {} has been finished.", threadName);
        } catch (InterruptedException e) {
            log.warn(">>>>>>> {} was just interrupted by {}",
                     threadName, e.getClass().getSimpleName());
            Thread.currentThread().interrupt();
        }
    }

}///:~