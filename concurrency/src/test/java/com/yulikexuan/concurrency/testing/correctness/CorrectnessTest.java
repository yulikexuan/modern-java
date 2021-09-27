//: com.yulikexuan.concurrency.testing.correctness.CorrectnessTest.java

package com.yulikexuan.concurrency.testing.correctness;


import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.*;

import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;


@Slf4j
@DisplayName("Correctness Test of SemaphoreBoundedBuffer - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CorrectnessTest {

    private static ThreadLocalRandom random;

    private static final int MIN_CAPACITY = 1_000;
    private static final int MAX_DATA_CAPACITY = 100_000;
    private static final long LOCKUP_DETECT_TIMEOUT = 1000L;
    private static final long BLOCKING_TIMEOUT = 1100L;


    private SemaphoreBoundedBuffer<Integer> boundedBuffer;

    @BeforeAll
    static void beforeAll() {
        random = ThreadLocalRandom.current();
    }

    static boolean isThreadAlive(@NonNull Thread thread) {
        try {
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            thread.interrupt();
            thread.join(LOCKUP_DETECT_TIMEOUT);
            return thread.isAlive();
        } catch (Exception unexpected) {
            fail();
            throw new IllegalStateException();
        }
    }

    @BeforeEach
    void setUp() {
        this.boundedBuffer = SemaphoreBoundedBuffer.of(MIN_CAPACITY);
    }

    private void fullBuffer() throws InterruptedException {
        IntStream.generate(random::nextInt).limit(MIN_CAPACITY)
                .forEach(i -> {
                    try {
                        boundedBuffer.put(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /*
     * Including a set of sequential tests in the test suite is often helpful,
     * since they can disclose when a problem is not related to concurrency
     * issues before the start looking for data races
     */
    @Nested
    @DisplayName("Sequentially Test the Correctness of BoundedBufferCt - ")
    class SequentialCorrectnessTest {

        @Test
        void test_The_Buffer_Should_Be_Empty_When_Constructed() {

            // Then
            assertThat(boundedBuffer.isEmpty()).isTrue();
            assertThat(boundedBuffer.isFull()).isFalse();
        }

        @Test
        void test_Being_Full_After_Put_All() throws InterruptedException {

            // Given
            fullBuffer();

            // When & Then
            assertThat(boundedBuffer.isFull()).isTrue();
            assertThat(boundedBuffer.isEmpty()).isFalse();
        }

    }//: End of SequentialCorrectnessTest

    @Nested
    @DisplayName("")
    class BlockingCorrectnessTest {

        private static final String TAKER_THREAD_NAME = "TAKER-THREAD";
        private static final String FULLING_THREAD_NAME = "FULLING-THREAD";

        @Test
        void test_Take_Method_Blocks_When_Empty() {

            // Given
            final Thread taker = new Thread(() -> {
                try {
                    int unused = boundedBuffer.take();
                    fail(); // if we get here, it's an error
                } catch (InterruptedException success) {
                    Thread.currentThread().interrupt();
                }
            });

            // When
            taker.start();

            // Then
            await().until(() -> !isThreadAlive(taker));
            assertThat(taker.isInterrupted()).isTrue();
        }

        @Test
        void test_Put_Method_Blocks_When_Full() throws InterruptedException {

            // Given
            fullBuffer();

            final Thread taker = new Thread(() -> {
                try {
                    boundedBuffer.put(1);
                    fail(); // if we get here, it's an error
                } catch (InterruptedException success) {
                    Thread.currentThread().interrupt();
                }
            });

            // When
            taker.start();

            // Then
            await().until(() -> !isThreadAlive(taker));
            assertThat(taker.isInterrupted()).isTrue();
        }

        @Test
        void given_Empty_Buffer_When_Take_Then_Blocking_With_Thread_Pool() throws Exception {

            // Given
            ThreadFactory tf = runnable -> new Thread(runnable, TAKER_THREAD_NAME);
            ExecutorService es = ExecutorServiceFactory.createSingleThreadExecutor(tf);

            Supplier<Integer> supplier = () -> {

                Integer number = null;

                String currentThreadName = Thread.currentThread().getName();

                try {
                    Integer unused = boundedBuffer.take();
                    // if we get here, it’s an error
                    Fail.fail(">>>>>>> The %s was not blocked.",
                            currentThreadName);
                } catch (InterruptedException success) {
                    log.info(">>>>>>> {} was interrupted.", currentThreadName);
                    Thread.currentThread().interrupt();
                }

                return number;
            };

            try {
                CompletableFuture.supplyAsync(supplier, es)
                        .get(BLOCKING_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.info(">>>>>>> TIMEOUT");
                assertThat(e).isExactlyInstanceOf(TimeoutException.class);
            }

        }

        @Test
        void given_Full_Buffer_When_Put_Then_Blocking_With_Thread_Pool() throws Exception {

            // Given
            fullBuffer();

            ThreadFactory tf = runnable -> new Thread(runnable, FULLING_THREAD_NAME);
            ExecutorService es = ExecutorServiceFactory.createSingleThreadExecutor(tf);

            Runnable runnable = () -> {
                try {
                    boundedBuffer.put(random.nextInt());
                    // if we get here, it’s an error
                    Fail.fail(">>>>>>> The %s was not blocked.",
                            Thread.currentThread().getName());
                } catch (InterruptedException success) {
                    log.info(">>>>>>> {} was interrupted.",
                            Thread.currentThread().getName());
                    Thread.currentThread().interrupt();
                }
            };

            try {
                CompletableFuture.runAsync(runnable, es)
                        .get(BLOCKING_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.info(">>>>>>> FULLING TIMEOUT");
                assertThat(e).isExactlyInstanceOf(TimeoutException.class);
            }

        }

    }//: End of Class BlockingCorrectnessTest

}///:~