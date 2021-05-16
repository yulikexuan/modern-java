//: com.yulikexuan.concurrency.state.OneShotLatchTest.java

package com.yulikexuan.concurrency.state;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;


@Slf4j
@DisplayName("Test a binary latch implemented using AQS - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OneShotLatchTest {

    private static final int WORKER_COUNT = 10;

    private static LongAdder doneCount;

    private LongAdder waitingCount;
    private Sync sync;
    private OneShotLatch oneShotLatch;

    @BeforeAll
    static void beforeAll() {
        doneCount = new LongAdder();
    }

    @BeforeEach
    void setUp() {
        this.sync = new Sync();
        this.waitingCount = new LongAdder();
        this.oneShotLatch = OneShotLatch.of(this.waitingCount, this.sync);
    }

    @Test
    void given_Workers_Then_Make_Them_Start_Together() {

        // Given
        for (int i = 0; i < WORKER_COUNT; ++i) {
            new Thread(new Worker(this.oneShotLatch)).start();
        }

        // When
        this.oneShotLatch.signal();

        // Then
        await().until(() -> doneCount.intValue() == WORKER_COUNT);
        assertThat(this.waitingCount.longValue()).isEqualTo(doneCount.longValue());
    }

    static class Worker implements Runnable {

        private final OneShotLatch startSignal;

        Worker(OneShotLatch startSignal) {
            this.startSignal = startSignal;
        }

        public void run() {
            try {
                startSignal.await();
                doWork();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        void doWork() {
            doneCount.increment();
        }

    }

}///:~