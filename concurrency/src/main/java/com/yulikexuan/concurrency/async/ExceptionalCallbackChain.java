//: com.yulikexuan.concurrency.async.ExceptionalCallbackChain.java

package com.yulikexuan.concurrency.async;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;


@Slf4j
final class ExceptionalCallbackChain {

    public static final String ERROR_MSG_ORIGINAL = ">>>>>> Exception from original";
    public static final String THE_FIRST_FUNC_INFO = ">>>>>> The first Func.";
    public static final String THE_SECOND_FUNC_INFO = ">>>>>> The second Func.";
    public static final String THE_FINAL_INFO = ">>>>>> The final process.";

    static CompletableFuture<Void> getCallbackChainWithErrorInTheCore(
            final ExecutorService executor) {

        return CompletableFuture.supplyAsync(
                () -> {
                    throw new RuntimeException(ERROR_MSG_ORIGINAL);
                }, executor).thenApply(s -> {
                    return THE_FIRST_FUNC_INFO;
                }).thenApply(s -> {
                    return THE_SECOND_FUNC_INFO;
                }).thenAccept(s -> log.info(THE_FINAL_INFO));
    }

}///:~