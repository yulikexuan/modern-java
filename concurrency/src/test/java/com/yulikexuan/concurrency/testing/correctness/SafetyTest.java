//: com.yulikexuan.concurrency.testing.correctness.SafetyTest.java

package com.yulikexuan.concurrency.testing.correctness;


import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Slf4j
@DisplayName("Safety Test of SemaphoreBoundedBuffer - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SafetyTest {

    private static final int DEFAULT_CAPACITY = 1000;

    // How many pairs of producer and consumer
    private static final int N_PAIRS = 10;

    private static final int N_TRIALS = 100_000;

    private static ExecutorService executor;

    private CyclicBarrier barrier;
    private SemaphoreBoundedBuffer<Integer> bb;

    private AtomicInteger putSum;
    private AtomicInteger takeSum;

    private BarrierTimer barrierTimer;

    @BeforeAll
    static void beforeAll() {
        ThreadPoolExecutor threadPoolExecutor =
                (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor = MoreExecutors.getExitingExecutorService(threadPoolExecutor);
    }

    @AfterAll
    static void afterAll() {
        executor.shutdown();
    }

    @BeforeEach
    void setUp() {
        this.bb = SemaphoreBoundedBuffer.of(DEFAULT_CAPACITY);
        this.barrierTimer = new BarrierTimer();
        this.barrier = new CyclicBarrier(N_PAIRS * 2 + 1, barrierTimer);
        this.putSum = new AtomicInteger(0);
        this.takeSum = new AtomicInteger(0);
    }

    @Test
    void test_Bounded_Buffer_Safety() throws Exception {

        for (int i = 0; i < N_PAIRS; i++) {
            this.executor.execute(BufferProducer.of(
                    this.bb, this.barrier, N_TRIALS, this.putSum));
            this.executor.execute(BufferConsumer.of(
                    this.bb, this.barrier, N_TRIALS, this.takeSum));
        }

        /*
         * the arrival index of the current thread, where index
         * getParties() - 1 indicates the first to arrive and zero
         * indicates the last to arrive
         */
        barrier.await(); // waiting for all threads to be ready

        barrier.await(); // waiting for all threads to be finished

        long throughput = this.barrierTimer.getThroughput(
                N_PAIRS * (long) N_TRIALS);

        log.info(">>>>>> Throughput is : {} ns/item", throughput);

        assertThat(putSum.get()).isEqualTo(takeSum.get());
    }

}///:~