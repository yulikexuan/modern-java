//: com.yulikexuan.concurrency.testing.correctness.SemaphoreBoundedBufferPutTakeTest.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Slf4j
@DisplayName("Safety Test of SemaphoreBoundedBuffer - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SemaphoreBoundedBufferPutTakeTest {

    private static final int DEFAULT_CAPACITY = 10;

    private static final int N_PAIRS = 10;
    private static final int N_TRIALS = 100_000;

    private static ExecutorService executor;

    protected CyclicBarrier barrier;
    protected SemaphoreBoundedBuffer<Integer> bb;

    protected AtomicInteger putSum;
    protected AtomicInteger takeSum;

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
        this.barrier = new CyclicBarrier(N_PAIRS * 2 + 1);
        this.putSum = new AtomicInteger(0);
        this.takeSum = new AtomicInteger(0);
    }

    @Test
    void test_Bounded_Buffer_Safety() {

        try {

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

            assertThat(putSum.get()).isEqualTo(takeSum.get());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}///:~