//: com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceTest.java


package com.yulikexuan.modernjava.concurrency.executors;


import com.yulikexuan.modernjava.concurrency.asyncapi.AsyncCalculation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * Keep code decoupled from the actual implementation of the thread pool
 */
public class ExecutorServiceTest {

    private LongAdder longAdder;
    private ExecutorService executorService;
    private CompletableFuture<Long> completableFuture;

    @BeforeEach
    void setUp() {
        this.longAdder = new LongAdder();
        this.completableFuture =new CompletableFuture<>();
    }

    @AfterEach
    void tearDown() {
        ExecutorServiceConfig.terminateExecutorServece(executorService);
    }

    /*
     * The Executor interface has only one single execute method to submit
     * Runnable instances for execution
     */
    @Test
    @DisplayName("Test Executor interface's single method - ")
    void test_Executor_Interface() {

        // Given
        this.executorService = Executors.newSingleThreadExecutor();

        Executor executor = this.executorService;

        // When
        executor.execute(() -> {
            this.longAdder.increment();

            // Then
            assertThat(this.longAdder.sumThenReset()).isEqualTo(1L);
        });
    }

    /*
     * ThreadPoolExecutor implements Executor, ExecutorService
     */
    @Test
    @DisplayName("Test ThreadPoolExecutor with fixed pool size - ")
    void test_Pool_Size_Of_Fixed_Thread_Pool_Executor() {

        // Given
        int numberOfThread = 2;
        int numberOfTasks = 3;
        this.executorService = Executors.newFixedThreadPool(2);

        // When
        final ThreadPoolExecutor threadPoolExecutor =
                (ThreadPoolExecutor) this.executorService;

        IntStream.range(0, numberOfTasks).forEach(i -> {
            threadPoolExecutor.submit(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        // Then
        assertThat(threadPoolExecutor.getPoolSize())
                .as("The pool size should still be %d",
                        numberOfThread)
                .isEqualTo(numberOfThread);

        /*
         * We created three tasks that imitate heavy work by sleeping for 1500
         * milliseconds
         *
         * The first two tasks will be executed at once, and the third one will
         * have to wait in the queue
         *
         * This can be verified by calling the getPoolSize() and
         * getQueue().size() methods immediately after submitting the tasks
         */
        assertThat(threadPoolExecutor.getQueue().size())
                .as("Should only hava one task left in the queue")
                .isEqualTo(numberOfTasks - numberOfThread);
    }

    /*
     * Executors.newCachedThreadPool()
     * This method does not receive a number of threads at all
     * The corePoolSize is actually set to 0
     * The maximumPoolSize is set to Integer.MAX_VALUE
     * The keepAliveTime is 60 seconds for this one.
     *
     * These parameter values mean that the cached thread pool may grow without
     * bounds to accommodate any amount of submitted tasks
     *
     * But when the threads are not needed anymore, they will be disposed of
     * after 60 seconds of inactivity
     *
     * A typical use case is when having a lot of short-living tasks in app
     */
    @Test
    @DisplayName("Test ThreadPoolExecutor with fixed pool size - ")
    void test_Pool_Size_Of_Cached_Thread_Pool() {

        // Given
        int numberOfTasks = 3;
        this.executorService = Executors.newCachedThreadPool();

        // When
        final ThreadPoolExecutor threadPoolExecutor =
                (ThreadPoolExecutor) this.executorService;

        IntStream.range(0, numberOfTasks).forEach(i -> {
            threadPoolExecutor.submit(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        // Then
        assertThat(threadPoolExecutor.getPoolSize())
                .as("The pool size should be %d", numberOfTasks)
                .isEqualTo(numberOfTasks);

        assertThat(threadPoolExecutor.getQueue().size())
                .as("Should hava no task left in the queue")
                .isEqualTo(numberOfTasks - numberOfTasks);
    }

    @Test
    @DisplayName("Test the pool size of SingleThreadExecutor - ")
    void test_Pool_Size_Of_Single_Thread_Pool() throws Exception {

        // Given
        int numberOfTasks = 3;
        this.executorService = Executors.newSingleThreadExecutor();

        // When

        /*
         * Additionally, this ThreadPoolExecutor is decorated with an immutable
         * wrapper, so it cannot be reconfigured after creation
         *
         * Note that also this is the reason we cannot cast it to a
         * ThreadPoolExecutor
         */
        IntStream.range(0, numberOfTasks).forEach(i -> {
            this.executorService.submit(() -> this.longAdder.increment());
        });

        // Then
        Thread.sleep(300);
        assertThat(this.longAdder.sumThenReset()).isEqualTo(numberOfTasks);
    }

    /*
     * The Executors.newScheduledThreadPool() method is typically used to
     * create a ScheduledThreadPoolExecutor with
     *   - A given corePoolSize,
     *   - Unbounded maximumPoolSize
     *   - 10L milliseconds keepAliveTime
     *
     * The zero time value will cause excess threads to terminate immediately
     * after executing tasks
     */
    @Test
    @DisplayName("Test how to create a ScheduledExecutorService - ")
    void test_Creating_Scheduled_Executor_Service() {

        // Given
        this.executorService = Executors.newScheduledThreadPool(
                ExecutorServiceConfig.NUMBER_OF_THREADS);

        ThreadPoolExecutor  threadPoolExecutor =
                (ThreadPoolExecutor)this.executorService;

        // When
        int corePoolSize = threadPoolExecutor.getCorePoolSize();
        int maxPoolSize = threadPoolExecutor.getMaximumPoolSize();
        long keepAliveTime = threadPoolExecutor.getKeepAliveTime(
                TimeUnit.MILLISECONDS);

        // Then
        assertThat(corePoolSize).isEqualTo(
                ExecutorServiceConfig.NUMBER_OF_THREADS);
        assertThat(maxPoolSize).isEqualTo(Integer.MAX_VALUE);
        assertThat(keepAliveTime).isEqualTo(10L);
    }

    @Test
    @DisplayName("Test Scheduled Executor Service Asychronously - ")
    void test_Scheduled_Executor_Service_Interface_Async() {

        // Given
        this.executorService = Executors.newScheduledThreadPool(
                ExecutorServiceConfig.NUMBER_OF_THREADS);

        final CompletableFuture<Long> completableFuture =
                new CompletableFuture<>();

        // When
        long start = System.nanoTime();

        ((ScheduledExecutorService) this.executorService).schedule(
                AsyncCalculation.getAsyncCalculation(completableFuture),
                AsyncCalculation.CALC_DURATION, TimeUnit.MILLISECONDS);

        long finalResult = completableFuture.join();

        long duration = (System.nanoTime() - start) / 1000_000;

        // Then
        assertThat(finalResult).isBetween(AsyncCalculation.FLOOR,
                AsyncCalculation.CEILLING);
        assertThat(duration).isBetween(AsyncCalculation.CALC_DURATION,
                AsyncCalculation.CALC_DURATION + 300);
    }

}///:~