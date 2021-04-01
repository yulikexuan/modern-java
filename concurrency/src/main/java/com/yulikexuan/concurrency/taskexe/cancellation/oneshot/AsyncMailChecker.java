//: com.yulikexuan.concurrency.taskexe.cancellation.oneshot.AsyncMailChecker.java

package com.yulikexuan.concurrency.taskexe.cancellation.oneshot;


import com.google.common.collect.ImmutableList;
import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
public class AsyncMailChecker {

    public static final String NEW_MAIL_FLAG = "NEW_MAIL";

    private ExecutorService executor;

    public boolean checkMail(@NonNull Set<String> hosts,
            long timeout, TimeUnit unit) throws Exception {

        this.executor = ExecutorServiceFactory.createFixedPoolSizeExecutor(
                Runtime.getRuntime().availableProcessors());

        List<CompletableFuture<Boolean>> futures = hosts.stream()
                .map(this::checkMailAtHost)
                .collect(ImmutableList.toImmutableList());

        return futures.stream()
                .anyMatch(future -> future.join());
    }

    private CompletableFuture<Boolean> checkMailAtHost(@NonNull String host) {

        try {
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean hasMail = StringUtils.endsWith(host, NEW_MAIL_FLAG);

        return CompletableFuture.supplyAsync(() -> false, this.executor);
    }

}///:~