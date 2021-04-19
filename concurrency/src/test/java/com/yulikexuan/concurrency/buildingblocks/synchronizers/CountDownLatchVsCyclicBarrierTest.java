//: com.yulikexuan.concurrency.buildingblocks.synchronizers.CountDownLatchVsCyclicBarrierTest.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers;


import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;


/**
 * {@see <a href="https://www.baeldung.com/java-cyclicbarrier-countdownlatch">Java CyclicBarrier vs CountDownLatch</a> }
 */
@Slf4j
@DisplayName("Comparing CountDownLatch with CyclicBarrier - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CountDownLatchVsCyclicBarrierTest {

    /*
     * A CountDownLatch maintains a count of tasks
     *
     * Task threads are counting down the latch while the main thread is waiting
     * for the latch's count to be 0
     *
     * The CountDownLatch is waiting for all tasks to be done
     */
    @Test
    void a_Single_Thread_Can_Count_Down_A_Latch_Twice() throws Exception {

        // Given
        final CountDownLatch latch = new CountDownLatch(2);

        final Thread thread = new Thread(() -> {
            latch.countDown();
            latch.countDown();
        });

        // When
        thread.start();
        latch.await();

        // Then
        assertThat(latch.getCount()).isZero();
    }

    @Test
    void a_Single_Thread_Can_Only_Wait_For_A_Barrier_Once() throws InterruptedException {

        // Given
        final int parties = 2;
        final CyclicBarrier barrier = new CyclicBarrier(parties);

        final Thread thread = new Thread(() -> {
            try {
                barrier.await();
                barrier.await();
                barrier.await();
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                log.error(">>>>>>> The thread was interrupted by {}",
                        e.getClass().getSimpleName());
                Thread.currentThread().interrupt();
            }
        });

        // When
        thread.start();

        // Then
        assertThat(barrier.getParties()).isEqualTo(parties);
        assertThat(barrier.getNumberWaiting()).isEqualTo(parties - 1);
        assertThat(barrier.isBroken()).isFalse();

        TimeUnit.MILLISECONDS.sleep(200L);
        thread.interrupt();
    }

    @Test
    void CyclicBarrier_Can_Be_Reset_Automatically() throws Exception {

        // Given
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7);

        ExecutorService es = ExecutorServiceFactory.createFixedPoolSizeExecutor(
                20);

        LongAdder outputScraper = new LongAdder();
        LongAdder finishedCount = new LongAdder();

        // When
        for (int i = 0; i < 20; i++) {
            es.execute(() -> {
                try {
                    if (cyclicBarrier.getNumberWaiting() <= 0) {
                        outputScraper.increment();
                    }
                    cyclicBarrier.await();
                    finishedCount.increment();
                } catch (InterruptedException | BrokenBarrierException e) {
                    log.error(">>>>>>> The thread was interrupted by {}",
                            e.getClass().getSimpleName());
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Then
        await().until(() -> outputScraper.longValue() >= (20 / 7));
        assertThat(finishedCount.longValue()).isEqualTo(14L);
        log.info(">>>>>>> {} not finished.", 20 - finishedCount.longValue());
    }

}///:~