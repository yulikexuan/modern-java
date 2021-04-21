//: com.yulikexuan.concurrency.testing.correctness.SemaphoreBoundedBufferPutTakeTest.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Slf4j
@DisplayName("Safety Test of SemaphoreBoundedBuffer - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SemaphoreBoundedBufferPutTakeTest {

    private static final int DEFAULT_CAPACITY = 10;

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
        executor = Executors.newCachedThreadPool();
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
        int finalIndex = barrier.await(); // waiting for all threads to be ready

        finalIndex = barrier.await(); // waiting for all threads to be finished

        long throughput = this.barrierTimer.getThroughput(
                N_PAIRS * (long) N_TRIALS);

        log.info(">>>>>> Throughput is : {} ns/item", throughput);

        assertThat(putSum.get()).isEqualTo(takeSum.get());
    }

    @Disabled("This is a performance test case which will need very long time to run")
    @Nested
    @DisplayName("Performance Test of SemaphoreBoundedBuffer - ")
    class PerformanceTest {

        private static final int TRIALS_PER_THREAD = 100_000;
        private static final int MAX_CAPACITY = 1000;
        private static final int CAPACITY_UPGRADE_STEP = 10;

        private static final int MAX_PAIRS = 128;
        private static final int PAIRS_UPGRADE_STEP = 2;

        @Test
        void test_Performance() throws Exception {

            // Given
            for (int capacity = 1;
                 capacity <= MAX_CAPACITY;
                 capacity *= CAPACITY_UPGRADE_STEP) {

                log.info(">>>>>>> The current capacity is {}", capacity);

                ExecutorService executor = null;
                SemaphoreBoundedBuffer<Integer> boundedBuffer = null;

                for (int pairs = 1;
                     pairs <= MAX_PAIRS;
                     pairs *= PAIRS_UPGRADE_STEP) {

                    log.info(">>>>>>> The current pairs is {}", pairs);

                    executor = Executors.newCachedThreadPool();

                    BarrierTimer barrierTimer = new BarrierTimer();
                    CyclicBarrier barrier = new CyclicBarrier(
                            pairs * 2 + 1, barrierTimer);
                    boundedBuffer = SemaphoreBoundedBuffer.of(capacity);

                    AtomicInteger putSum = new AtomicInteger(0);
                    AtomicInteger takeSum = new AtomicInteger(0);

                    for (int i = 0; i < pairs; i++) {
                        executor.execute(BufferProducer.of(boundedBuffer,
                                barrier, TRIALS_PER_THREAD, putSum));
                        executor.execute(BufferConsumer.of(boundedBuffer,
                                barrier, TRIALS_PER_THREAD, takeSum));
                    }

                    barrier.await();
                    barrier.await();

                    long throughput = barrierTimer.getThroughput(
                            pairs * (long) TRIALS_PER_THREAD);

                    log.info(">>>>>> {} capacity / {} pairs throughput is : {} ns/item",
                            capacity, pairs, throughput);

                    assertThat(putSum.get()).isEqualTo(takeSum.get());

                    executor.shutdown();
                    executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
                    if (!executor.isTerminated()) {
                        executor.shutdownNow();
                    }
                    TimeUnit.MILLISECONDS.sleep(1000);
                }

                TimeUnit.MILLISECONDS.sleep(1000);
            }

        }

    }

}///:~