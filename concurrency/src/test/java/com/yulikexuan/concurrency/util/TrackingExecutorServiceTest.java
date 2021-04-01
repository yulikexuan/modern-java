//: com.yulikexuan.concurrency.util.TrackingExecutorServiceTest.java

package com.yulikexuan.concurrency.util;


import com.google.common.collect.ImmutableList;
import com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable.CancellableSocketTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;


@Slf4j
@DisplayName("Test the Tracking Abilities of TrackingExecutorService - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TrackingExecutorServiceTest {

    private static final int TASK_COUNT = 4;
    private static final int EXPECTED_CANCELLED_TASK_COUNT = 3;

    private static final Duration LONG_RUNNING_TIME = Duration.ofHours(1L);
    private static final Duration SHORT_RUNNING_TIME = Duration.ofMillis(100L);
    private static final Duration EXECUTOR_RUNNING_TIME = Duration.ofMillis(200L);
    private static final Duration EXECUTOR_TERMINATION_TIME_OUT = Duration.ofMillis(110L);

    private List<Runnable> runnableTasks;
    private List<CallableTask<String>> callableTasks;

    private TrackingExecutorService trackingExecutor;

    @BeforeEach
    void setUp() throws Exception {

        this.runnableTasks = List.of(
                RunnableTask.of(LONG_RUNNING_TIME),
                RunnableTask.of(LONG_RUNNING_TIME),
                RunnableTask.of(SHORT_RUNNING_TIME),
                RunnableTask.of(LONG_RUNNING_TIME));

        this.callableTasks = List.of(
                CallableTask.of(LONG_RUNNING_TIME, "1"),
                CallableTask.of(LONG_RUNNING_TIME, "2"),
                CallableTask.of(SHORT_RUNNING_TIME, "3"),
                CallableTask.of(LONG_RUNNING_TIME, "4"));

        this.trackingExecutor = ExecutorServiceFactory
                .createTrackingFixedPoolSizeExecutor(TASK_COUNT,
                        EXECUTOR_TERMINATION_TIME_OUT);
    }

    @Test
    void able_To_Know_Which_Executed_Runnable_Tasks_Were_Cancelled()
            throws Exception {

        // Given
        for (Runnable runnable : this.runnableTasks) {
            this.trackingExecutor.execute(runnable);
        }

        // When
        TimeUnit.MILLISECONDS.sleep(EXECUTOR_RUNNING_TIME.toMillis());
        this.trackingExecutor.shutdownNow();

        // Then
        await().until(() -> this.trackingExecutor.getCancelledTasks().size() > 0);
        assertThat(this.trackingExecutor.getCancelledTasks().size()).isEqualTo(
                EXPECTED_CANCELLED_TASK_COUNT);
    }

    @Test
    void able_To_Know_Which_Submitted_Runnable_Tasks_Were_Cancelled()
            throws Exception {

        // Given
        for (Runnable runnable : this.runnableTasks) {
            this.trackingExecutor.submit(runnable);
        }

        // When
        TimeUnit.MILLISECONDS.sleep(EXECUTOR_RUNNING_TIME.toMillis());
        this.trackingExecutor.shutdownNow();

        // Then
        await().until(() -> this.trackingExecutor.getCancelledTasks().size() > 0);
        assertThat(this.trackingExecutor.getCancelledTasks().size()).isEqualTo(
                EXPECTED_CANCELLED_TASK_COUNT);
    }

    @Test
    void able_To_Know_Which_Submitted_Callable_Tasks_Were_Cancelled()
            throws Exception {

        // Given
        for (Callable<String> callable: this.callableTasks) {
            this.trackingExecutor.submit(callable);
        }

        // When
        TimeUnit.MILLISECONDS.sleep(EXECUTOR_RUNNING_TIME.toMillis());
        this.trackingExecutor.shutdownNow();

        // Then
        await().until(() -> this.trackingExecutor.getCancelledTasks().size() > 0);
        assertThat(this.trackingExecutor.getCancelledTasks().size()).isEqualTo(
                EXPECTED_CANCELLED_TASK_COUNT);
    }

    @Test
    void able_To_Know_Which_Runnable_CompletableFuture_Were_Cancelled() throws Exception {

        // Given
        List<CompletableFuture<Void>> futures = this.runnableTasks.stream()
                .map(task -> CompletableFuture.runAsync(
                        task, this.trackingExecutor))
                .collect(ImmutableList.toImmutableList());

        // When
        TimeUnit.MILLISECONDS.sleep(EXECUTOR_RUNNING_TIME.toMillis());
        this.trackingExecutor.shutdownNow();

        // Then
        await().until(() -> this.trackingExecutor.getCancelledTasks().size() > 0);
        assertThat(this.trackingExecutor.getCancelledTasks().size()).isEqualTo(
                EXPECTED_CANCELLED_TASK_COUNT);
    }

    @Test
    void able_To_Know_Which_Callable_CompletableFuture_Were_Cancelled() throws Exception {

        // Given
        List<CompletableFuture<String>> futures = this.callableTasks.stream()
                .map(task -> CompletableFuture.supplyAsync(
                        task::call, this.trackingExecutor))
                .collect(ImmutableList.toImmutableList());

        // When
        TimeUnit.MILLISECONDS.sleep(EXECUTOR_RUNNING_TIME.toMillis());
        this.trackingExecutor.shutdownNow();

        // Then
        await().until(() -> this.trackingExecutor.getCancelledTasks().size() > 0);
        assertThat(this.trackingExecutor.getCancelledTasks().size()).isEqualTo(
                EXPECTED_CANCELLED_TASK_COUNT);
    }

    @Test
    void able_To_Know_Which_Uninterruptable_Tasks_Were_Cancelled() throws Exception {

        // Given
        int port = 8089;
        String host = "127.0.0.1";
        ServerSocket serverSocket = new ServerSocket(port);
        List<CancellableSocketTask> socketTasks = List.of(
                CancellableSocketTask.of(new Socket(host, port)),
                CancellableSocketTask.of(new Socket(host, port)),
                CancellableSocketTask.of(new Socket(host, port)),
                CancellableSocketTask.of(new Socket(host, port))
        );

        List<Future<Void>> futureTasks = socketTasks.stream()
                .map(this.trackingExecutor::submit)
                .collect(ImmutableList.toImmutableList());

        // When
        TimeUnit.MILLISECONDS.sleep(EXECUTOR_RUNNING_TIME.toMillis());

        this.trackingExecutor.shutdownNow();

        // Then
        await().until(() -> this.trackingExecutor
                .getCancelledUninterruptableTasks().size() > 0);
        assertThat(this.trackingExecutor.getCancelledUninterruptableTasks()
                .size()).isEqualTo(socketTasks.size());
    }

}///:~