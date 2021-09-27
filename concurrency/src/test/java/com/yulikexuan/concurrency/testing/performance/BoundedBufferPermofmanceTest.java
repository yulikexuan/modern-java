//: com.yulikexuan.concurrency.testing.performance.BoundedBufferPermofmanceTest.java

package com.yulikexuan.concurrency.testing.performance;


import com.yulikexuan.concurrency.testing.correctness.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Slf4j
@DisplayName("Test BoundedBufferPermofmanceTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled("This is a performance test case which will need very long time to run")
class BoundedBufferPermofmanceTest {

    private static final int DEFAULT_CAPACITY = 1000;

    // How many pairs of producer and consumer
    private static final int TRIALS_PER_THREAD = 100_000;
    private static final int MAX_CAPACITY = 1000;
    private static final int CAPACITY_UPGRADE_STEP = 10;
    private static final int MAX_PAIRS = 128;
    private static final int PAIRS_UPGRADE_STEP = 2;

    @Test
    void test_Performance_For_BoundedBuffer() throws Exception {

        // Given
        // Capacity: 1, 10, 100, 1000
        for (int capacity = 1000; // int capacity = 1;
             capacity <= MAX_CAPACITY;
             capacity *= CAPACITY_UPGRADE_STEP) {

            log.info(">>>>>>> The current capacity is {}", capacity);

            ExecutorService executor = null;
            SemaphoreBoundedBuffer<Integer> boundedBuffer = null;

            // Pairs: 1, 2, 4, 8, ..., 128
            for (int pairs = 64; // int pairs = 1;
                 pairs <= 64; // pairs <= MAX_PAIRS;
                 pairs *= PAIRS_UPGRADE_STEP) {

                log.info(">>>>>>> The current pairs is {}", pairs);

                TestingThreadFactory threadFactory = TestingThreadFactory.of(
                        Executors.defaultThreadFactory());

                executor = Executors.newCachedThreadPool(threadFactory);

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
                    List<Runnable> neverCommencedTasks = executor.shutdownNow();
                    log.info(">>>>>>> Never commenced tasks number: {}",
                            neverCommencedTasks.size());
                }

                long totalThreadCreated = threadFactory.getThreadNumberOfCreated();

                log.info(">>>>>>> {} threads were created for {} pairs",
                        totalThreadCreated, pairs);

                assertThat(totalThreadCreated).isEqualTo(pairs * 2);

                TimeUnit.MILLISECONDS.sleep(500);

            }// End of for (int pairs)

            TimeUnit.MILLISECONDS.sleep(500);

        } // End of for (int capacity)

    }

}///:~