//: com.yulikexuan.concurrency.testing.correctness.ThreadPoolTest.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Slf4j
@DisplayName("Using callbacks to test Thread Pool - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ThreadPoolTest {

    private static final int MAX_SIZE = 10;
    private static final long MAX_TASK_NUMBER = MAX_SIZE * 10;

    private ExecutorService executor;
    private TestingThreadFactory threadFactory;

    @BeforeEach
    void setUp() {
        this.threadFactory = TestingThreadFactory.of(
                Executors.defaultThreadFactory());
        this.executor = Executors.newFixedThreadPool(MAX_SIZE, this.threadFactory);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.executor.shutdown();
        this.executor.awaitTermination(300, TimeUnit.MILLISECONDS);
        if (!this.executor.isTerminated()) {
            List<Runnable> notRun = this.executor.shutdownNow();
            log.warn(">>>>>>> {} threads are never run.", notRun.size());
        }
    }

    @Test
    void test_Pool_Expansion() throws Exception {

        // Given
        for (int i = 0; i < MAX_TASK_NUMBER; i++) {
            this.executor.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // When
        for (int i = 0; i < 20 && this.threadFactory
                .getThreadNumberOfCreated() < MAX_SIZE; i++) {

            TimeUnit.MILLISECONDS.sleep(100L);
        }

        // Then
        assertThat(this.threadFactory.getThreadNumberOfCreated())
                .isEqualTo(MAX_SIZE);
    }

}///:~