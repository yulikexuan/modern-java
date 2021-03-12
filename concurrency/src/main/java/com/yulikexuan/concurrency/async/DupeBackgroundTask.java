//: com.yulikexuan.concurrency.async.DupeBackgroundTask.java

package com.yulikexuan.concurrency.async;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;


@Slf4j
public class DupeBackgroundTask implements Runnable {

    private final Duration duration;

    private DupeBackgroundTask(Duration duration) {
        this.duration = duration;
    }

    public static DupeBackgroundTask of(@NonNull Duration duration) {
        return new DupeBackgroundTask(duration);
    }

    @Override
    public void run() {

        try {
            Thread.sleep(this.duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(">>>>>> DupeBackgroundTask was Inturrpted");
        }

        log.info(">>>>>>> DupeBackgroundTask was just completed!");
    }

}///:~