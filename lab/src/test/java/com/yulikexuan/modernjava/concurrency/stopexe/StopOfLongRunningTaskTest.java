//: com.yulikexuan.modernjava.concurrency.stopexe.StopOfLongRunningTaskTest.java


package com.yulikexuan.modernjava.concurrency.stopexe;


import com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;


@DisplayName("Test Stopping long running task - ")
class StopOfLongRunningTaskTest {

    private LongRunningTask longRunningTask;

    @BeforeEach
    void setUp() {
        this.longRunningTask = LongRunningTask.of();
    }

    @Test
    void test_Stopping_Long_Running_Task_With_ExecutorService() {

        // Given
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future future = executorService.submit(this.longRunningTask);

        // When
        try {
            future.get(200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            future.cancel(true);
        } finally {
            ExecutorServiceConfig.terminateExecutorService(executorService);
        }

    }

    @Test
    void test_Stopping_Long_Running_Task_With_ScheduledExecutorService() {

        // Given
        ScheduledExecutorService executorService =
                Executors.newScheduledThreadPool(2);

        final Future future = executorService.submit(this.longRunningTask);

        // When
        executorService.schedule(() -> future.cancel(true),
                200L, TimeUnit.MILLISECONDS);

        ExecutorServiceConfig.terminateExecutorService(executorService);
    }

}///:~