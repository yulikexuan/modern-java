//: com.yulikexuan.effectivejava.concurrency.threadlocal.SessionManager.java

package com.yulikexuan.effectivejava.concurrency.threadlocal;


import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;


@Slf4j
class SessionManager {

    private static final int EXECUTOR_POOL_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 1000;
    private static final Duration TERMINATION_TIMEOUT_DURATION =
            Duration.ofMillis(1000);

    static List<String> startSessions() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                EXECUTOR_POOL_SIZE,
                EXECUTOR_POOL_SIZE,
                1000,
                TimeUnit.MILLISECONDS,
                new LinkedTransferQueue<>());

        executor.allowCoreThreadTimeOut(true);

        ExecutorService executorService = MoreExecutors.getExitingExecutorService(
                executor, TERMINATION_TIMEOUT_DURATION);

        List<Future<String>> futureSessionContexts =
                UserRepository.INSTANCE.getAllUserIds().stream()
                        .map(Session::of)
                        .map(session -> executorService.submit(session))
                        .collect(ImmutableList.toImmutableList());

        return futureSessionContexts.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return null;
                    }
                }).collect(ImmutableList.toImmutableList());
    }

}///:~