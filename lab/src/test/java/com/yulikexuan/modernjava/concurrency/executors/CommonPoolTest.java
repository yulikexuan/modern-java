//: com.yulikexuan.modernjava.concurrency.executors.CommonPoolTest.java


package com.yulikexuan.modernjava.concurrency.executors;


import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("ForkJoinPool # Common Pool Tests - ")
public class CommonPoolTest {

    /*
     * Two major concepts use the commonPool inside JDK
     *   - CompletableFuture: able to specify other thread-pool to use
     *   - Parallel Streams: not able to use specified thread-pool
     *
     * When to use Which
     *   1. Do not always use common pool for all cases below:
     *      - The key thing whether to use commonPool or not is the purpose of
     *        the task which will be passed to the thread pool
     *      - In general, there are two kinds of tasks
     *        - Computational Tasks
     *        - Blocking Tasks
     *
     *   2. Computational Tasks: must use common pool
     *      No any blocking such as I/O operation
     *        - DB Invocation
     *        - Synchronization
     *        - Thread Sleep
     *        - Scheduled tasks
     *        - etc...
     *      The trick is that it does not matter on which thread your task is
     *      running, just keep the CPU busy and don't wait for any resources
     *
     *   3. Blocking Tasks: must create a new thread-pool for blocking tasks and
     *      keep the rest of the system separated and predictable
     *
     *      There are some consequences:
     *      - If having more than three available CPUs, then the commonPool
     *        is automatically sized to two threads and you can very easily
     *        block execution of any other part of your system that uses the
     *        commonPool at the same time by keeping the threads in a blocked
     *        state
     *
     *      As a rule of thumb, we can create our own thread-pool for blocking
     *      tasks and keep the rest of the system separated and predictable
     *
     * There are three modes you can achieve in commonPool:
     *    - parallelism > 2 : JDK creates the (# of CPUs - 1) threads for the
     *                        commonPool
     *    - parallelism = 1 : JDK creates new thread for every submitted task
     *    - parallelism = 0 : A submitted task is executed on a caller thread
     */
    static ForkJoinPool commonPool;
    static int targetParallelism;
    static int commonPoolParallelism;
    static int commonPoolCommonParallelism;

    @BeforeAll
    static void beforeAll() {
        commonPool = ForkJoinPool.commonPool();
        targetParallelism = Runtime.getRuntime().availableProcessors();
        commonPoolParallelism = commonPool.getParallelism();
        commonPoolCommonParallelism = ForkJoinPool.getCommonPoolParallelism();
    }

    @BeforeEach
    void setUp() {
        log.info(">>>>>>> The target parallelism: {}", targetParallelism);
        log.info(">>>>>>> The commonPool parallelism: {}", commonPoolParallelism);
        log.info(">>>>>>> The commonPool common parallelism: {}",
                commonPoolCommonParallelism);
    }

    @Test
    void test_The_Target_Parallelism_Level() {
        assertThat(commonPoolCommonParallelism).isEqualTo(targetParallelism - 1);
    }

    @Test
    @Disabled("This is a test for blocking tasks that are running on common pool")
    void test_Blocking_IO_Tasks_With_Common_Pool() {

        // Given
        long start = System.nanoTime();

        List<CompletableFuture<Void>> futures = IntStream.range(0, 50)
                .mapToObj(i -> CompletableFuture.runAsync(
                        CommonPoolTest::blockingOperation))
                .collect(ImmutableList.toImmutableList());

        // When
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        Duration suprise = Duration.ofNanos(System.nanoTime() - start);

        // Then
        assertThat(suprise).isBetween(Duration.ofSeconds(5), Duration.ofSeconds(10));
        System.out.println("Processed in " +
                Duration.ofNanos(System.nanoTime() - start).toSeconds() + " sec");
    }

    @Test
    void test_Blocking_IO_Tasks_With_Customized_Pool() {

        // Given
        long start = System.nanoTime();

        ExecutorService executorService = Executors.newCachedThreadPool();

        List<CompletableFuture<Long>> futures = IntStream.range(0, 50)
                .mapToObj(i -> {
                    CompletableFuture<Long> completableFuture =
                            new CompletableFuture<>();
                    executorService.submit(() -> blockingTask(i, completableFuture));
                    return completableFuture;
                })
                .collect(ImmutableList.toImmutableList());

        futures.forEach(CompletableFuture::join);

        // When
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        Duration suprise = Duration.ofNanos(System.nanoTime() - start);

        // Then
        assertThat(suprise).isBetween(Duration.ofSeconds(0), Duration.ofSeconds(3));
        System.out.println("Processed in " +
                Duration.ofNanos(System.nanoTime() - start).toSeconds() + " sec");
    }

    private static void blockingOperation() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void blockingTask(long index,
            final CompletableFuture<Long> completableFuture) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        completableFuture.complete(index);
    }

}///:~