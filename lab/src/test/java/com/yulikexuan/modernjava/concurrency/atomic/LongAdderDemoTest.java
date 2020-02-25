//: com.yulikexuan.modernjava.concurrency.atomic.LongAdderDemoTest.java


package com.yulikexuan.modernjava.concurrency.atomic;


import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static com.yulikexuan.modernjava.concurrency.ExecutorServiceConfiguration.*;


class LongAdderDemoTest {

    private List<Runnable> demoActions;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        this.demoActions = Lists.newArrayList();
        IntStream.range(0, NUMBER_OF_THREADS).forEach(i ->
                demoActions.add(LongAdderDemo.of().getIncrementAction()));
        this.executorService = Executors.newFixedThreadPool(SIZE_OF_THREAD_POOL);
    }

    @AfterEach
    void tearDown() {
        this.executorService.shutdown();
        try {
            if (!this.executorService.awaitTermination(
                    TERMINATE_TIMEOUT_MILLISECOND, TimeUnit.MILLISECONDS)) {
                this.executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executorService.shutdownNow();
        }
    }

    @Test
    void test_Increment_LongAdder_Parallel() {

        // Given

        // When
        this.demoActions.stream().forEach(
                action -> this.executorService.submit(action));

        // Then
        assertAll("Validate accumulating result: ", () -> {
                    assertThat(LongAdderDemo.sumThenReset()).isEqualTo(
                            NUMBER_OF_INCREMENTS * NUMBER_OF_THREADS);
                    assertThat(LongAdderDemo.sum()).isEqualTo(0); });
    }

}///:~