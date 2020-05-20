//: com.yulikexuan.modernjava.concurrency.executors.DelayedExecutorTest.java


package com.yulikexuan.modernjava.concurrency.executors;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Delayed Executor Creation Test - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DelayedExecutorTest {

    private static LongAdder count;

    private StopWatch stopWatch;

    @BeforeAll
    static void init() {
        count = new LongAdder();
    }

    @BeforeEach
    void setUp() {
        this.stopWatch = StopWatch.createStarted();
    }

    @AfterEach
    void tearDown() {
        this.count.reset();
    }

    private long addCount() {
        count.increment();
        this.stopWatch.stop();
        return this.stopWatch.getTime();
    }

    @Test
    void test_Given_A_Runnable_Action_Then_Running_On_A_Delayed_Executor()
            throws InterruptedException {

        // Given
        long deferredMillis = 200L;
        long maxDelayedMillis = deferredMillis + 100L;
        long terminationTimeoutMillis = 1000L;

        Executor delayedExecutor = ExecutorServiceFactory.newDelayedExecutor(
                deferredMillis, terminationTimeoutMillis);

        // When
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(
                this::addCount, delayedExecutor);
        long consumingTime = future.join();

        // Then
        assertThat(consumingTime)
                .isGreaterThan(deferredMillis)
                .isLessThan(maxDelayedMillis);
    }

}///:~