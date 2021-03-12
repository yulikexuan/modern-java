//: com.yulikexuan.concurrency.async.DupeSecretBackgroundTask.java

package com.yulikexuan.concurrency.async;


import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Duration;
import java.util.function.Supplier;


public class DupeSecretBackgroundTask implements Supplier<String> {

    static final int SECRET_LENGTH = 17;

    private final Duration duration;

    private DupeSecretBackgroundTask(Duration duration) {
        this.duration = duration;
    }

    public static DupeSecretBackgroundTask of(@NonNull Duration duration) {
        return new DupeSecretBackgroundTask(duration);
    }

    @Override
    public String get() {
        try {
            Thread.sleep(this.duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(
                    ">>>>>> DupeSecretBackgroundTask was Inturrpted");
        }

        return RandomStringUtils.randomAlphanumeric(SECRET_LENGTH);
    }

}///:~