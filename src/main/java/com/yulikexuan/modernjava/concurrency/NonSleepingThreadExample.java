//: com.yulikexuan.modernjava.concurrency.NonSleepingThreadExample.java


package com.yulikexuan.modernjava.concurrency;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NonSleepingThreadExample {

    public static void main(String[] args) {

        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);

        scheduledExecutorService.schedule(
                () -> log.info(">>>>>>> The Before Part of my task."), 0,
                TimeUnit.SECONDS);

        scheduledExecutorService.schedule(
                () -> log.info(">>>>>>> The After Part of my task."), 1,
                TimeUnit.SECONDS);

        scheduledExecutorService.shutdown();
        try {
            if (!scheduledExecutorService.awaitTermination(1,
                    TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdown();
            }
        } catch (InterruptedException ie) {
            scheduledExecutorService.shutdown();
        }
    }

}///:~