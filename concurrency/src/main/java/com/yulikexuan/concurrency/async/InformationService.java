//: com.yulikexuan.concurrency.async.InformationService.java

package com.yulikexuan.concurrency.async;


import lombok.NonNull;

import java.time.Duration;
import java.util.Base64;
import java.util.function.Function;


public class InformationService implements Function<byte[], String> {

    static final int SECRET_LENGTH = 17;

    private final Duration duration;

    private InformationService(Duration duration) {
        this.duration = duration;
    }

    public static InformationService of(@NonNull Duration duration) {
        return new InformationService(duration);
    }

    @Override
    public String apply(byte[] secret) {
        try {
            Thread.sleep(this.duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(
                    ">>>>>> DupeSecretBackgroundTask was Inturrpted");
        }

        return Base64.getEncoder().encodeToString(secret);
    }

}///:~