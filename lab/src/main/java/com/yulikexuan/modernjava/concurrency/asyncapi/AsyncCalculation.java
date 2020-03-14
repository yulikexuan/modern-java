//: com.yulikexuan.modernjava.concurrency.asyncapi.AsyncCalculation.java


package com.yulikexuan.modernjava.concurrency.asyncapi;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@Getter
@AllArgsConstructor(staticName = "of")
public class AsyncCalculation {

    public static final long FLOOR = 1000;
    public static final long CEILLING = 1200;
    public static final long CALC_DURATION = 500;

    private static ThreadLocalRandom random = ThreadLocalRandom.current();

    public static Runnable getAsyncCalculation(
            final CompletableFuture<Long> completableFuture) {

        Objects.requireNonNull(completableFuture, "Needs a " +
                "CompletableFuture instance to calculate asynchronously.");

        return () -> {
            final long result = random.nextLong(1000, 1100);
            completableFuture.complete(result);
            log.info(">>>>>>> Calculated asynchronously: {}", result);
        };
    }

}///:~
