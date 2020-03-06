//: com.yulikexuan.modernjava.concurrency.atomic.LongAccumulatorDemoTest.java


package com.yulikexuan.modernjava.concurrency.atomic;


import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceConfiguration.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class LongAccumulatorDemoTest {

    private List<Runnable> demoActions;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        this.demoActions = Lists.newArrayList();
        LongAccumulatorDemo demo = new LongAccumulatorDemo();
        IntStream.range(0, NUMBER_OF_THREADS).forEach(i ->
                demoActions.add(demo.getAccumulateAction()));
        this.executorService = Executors.newFixedThreadPool(SIZE_OF_THREAD_POOL);
    }

    @AfterEach
    void tearDown() {
        terminateExecutorServece(executorService);
    }

    @Test
    void getThenReset() {

        // Given

        // When
        this.demoActions.stream().forEach(
                action -> this.executorService.submit(action));

        // Then
        assertAll("Validate accumulating result: ", () -> {
            assertThat(this.demoActions.size()).isEqualTo(NUMBER_OF_THREADS);
            assertThat(LongAccumulatorDemo.getThenReset()).isEqualTo(20200);
            assertThat(LongAccumulatorDemo.get()).isEqualTo(0); });
    }

}///:~