//: com.yulikexuan.concurrency.taskexe.cancellation.NoncancelableTaskTest.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("Test Non-Cancelable Task - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NoncancelableTaskTest {

    private static final int CAPACITY = 6;

    @Mock
    private Task task;

    private static StopWatch stopWatch;
    private static ExecutorService executor;

    private NoncancelableTask noncancelableTask;

    private BlockingQueue<Task> taskQueue;

    @BeforeAll
    static void beforeAll() throws Exception {
        stopWatch = StopWatch.create();
        executor = ExecutorServiceFactory.createSingleThreadExecutor();
    }

    @BeforeEach
    void setUp() throws Exception {
        this.taskQueue = new LinkedBlockingQueue<>(CAPACITY);
        this.noncancelableTask = NoncancelableTask.of(this.taskQueue);
    }

    @Test
    void is_Cancelable_With_Interrupting_The_Working_Thread_Borrowed_From_ExecutorService() throws Exception {

        // Given
        CompletableFuture<Task> futureTask = CompletableFuture.supplyAsync(
                () -> this.noncancelableTask.getNextTask(),
                executor);

        TimeUnit.MILLISECONDS.sleep(200);

        futureTask.cancel(true);

        // When & Then
        assertThat(futureTask.isCancelled()).isTrue();
        assertThat(futureTask.isDone()).isTrue();
        assertThat(futureTask.isCompletedExceptionally()).isTrue();
    }

    @Test
    void is_Not_Cancelable_With_Single_Independent_Thread() throws Exception {

        // Given
        Thread taskThread = new Thread(
                () -> this.noncancelableTask.getNextTask());

        taskThread.start();

        TimeUnit.MILLISECONDS.sleep(200);

        // When
        taskThread.interrupt();

        // When & Then
    }

}///:~