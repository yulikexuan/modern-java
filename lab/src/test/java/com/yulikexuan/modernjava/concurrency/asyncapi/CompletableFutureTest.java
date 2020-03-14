//: com.yulikexuan.modernjava.concurrency.asyncapi.CompletableFutureTest.java


package com.yulikexuan.modernjava.concurrency.asyncapi;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTimeout;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("CompletableFuture Test - ")
public class CompletableFutureTest {

    @BeforeEach
    void setUp() {
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nested
    @DisplayName("Test Using CompletableFuture as a Simple Future - ")
    class UsingAsASimpleFutureTest {

        @Test
        void test_Create_CompletableFuture_With_Known_Result() throws Exception {

            // Given
            String result = "Hello Asynchronous Programming!";
            CompletableFuture<String> completableFuture =
                    CompletableFuture.completedFuture(result);

            // When
            String actualResult = completableFuture.get();

            // Then
            assertThat(actualResult).isEqualTo(result);
        }

        @Test
        void test_Cancelling_CompletableFuture() throws Exception {

            // Given
            CompletableFuture<String> completableFuture = new CompletableFuture<>();

            Executors.newFixedThreadPool(1).submit(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                completableFuture.cancel(false);
                return null;
            });

            // When
            assertThatThrownBy(() -> completableFuture.get()).isInstanceOf(
                    CancellationException.class);
        }

    }//: End of class UsingAsASimpleFutureTest

    @Nested
    @DisplayName("Test Using CompletableFuture with Encapsulaed Computation Logic - ")
    class UsingWithEncapsulatedComputationLogicTest {

        /*
         * Skip the boilerplate and simply execute some code asynchronously
         * with CompletableFuture::runAsync which returns a new CompletableFuture
         * that is asynchronously completed by a task running in the
         * ForkJoinPool.commonPool() after it runs the given action
         */
        @Test
        void test_Encapsulated_Computation_Logic_runAsync() {

            // Given
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(
                    () -> {
                        try {
                            Thread.sleep(500);
                            System.out.println(">>>>>>> Hello Asynchronous Programming!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

            // When & Then
            assertTimeout(Duration.ofMillis(510), () -> completableFuture.join());
        }

        /*
         * CompletableFuture::supplyAsync Returns a new CompletableFuture that is
         * asynchronously completed by a task running in the ForkJoinPool.commonPool()
         * with the value obtained by calling the given Supplier.
         */
        @Test
        void test_Encapsulated_Computation_Logic_supplyAsync() {

            // Given
            long duration = 500;
            long timeout = duration + 100;
            long start = 10000;
            long ceiling = 100000;
            CompletableFuture<Long> completableFuture =
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            Thread.sleep(duration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ThreadLocalRandom.current()
                                .nextLong(start, ceiling);
                    });

            // When & Then
            assertTimeout(Duration.ofMillis(timeout), () ->
                    assertThat(completableFuture.join()).isBetween(start, ceiling));
        }

    }//: End of class UsingWithEncapsulatedComputationLogicTest

    @Nested
    @DisplayName("Test Processing Results of Asynchronous Computations - ")
    class ProcessingResultsOfAsyncComputationsTest {

        /*
         * CompletionStage::thenApply Returns a new CompletionStage that,
         * when this stage completes normally, is executed with this stage's result
         * as the argument to the supplied function
         * This method is analogous to Optional.map and Stream.map.
         *
         * CompletionStage::thenApplyAsync Returns a new CompletionStage that,
         * when this stage completes normally, is executed using this stage's
         * default asynchronous execution facility, with this stage's result as the
         * argument to the supplied functionn
         */
        @Test
        void test_Processing_Results_With_thenApplyAsync_Function() {

            // Given
            CompletableFuture<String> secretKeyFuture =
                    CompletableFuture.supplyAsync(() ->
                            RandomStringUtils.randomAlphanumeric(10))
                            .thenApplyAsync(key -> {
                                sleep(200);
                                return StringUtils.upperCase(key);
                            });

            // When
            System.out.println(">>>>>>> Secret key is coming soon ......");
            /*
             * The CompletableFuture.join() method is similar to the get method,
             * but it throws an unchecked exception in case the Future does not
             * complete normally.
             * This makes it possible to use it as a method reference in the
             * Stream.map() method.
             */
            String fullSecretKey = secretKeyFuture.join();

            System.out.println(fullSecretKey);

            // Then
            assertThat(fullSecretKey.length()).isEqualTo(10);
            assertThat(StringUtils.isAlphanumeric(fullSecretKey)).isTrue();
        }

        @Test
        void test_Processing_Results_With_thenAcceptAsync_Consumer() {

            // Given
            CompletableFuture<Void> secretKeyPrintFuture =
                    CompletableFuture.supplyAsync(
                            () -> RandomStringUtils.randomAlphanumeric(10))
                            .thenAcceptAsync(key -> {
                                sleep(200);
                                System.out.println(">>>>>>> " +
                                        StringUtils.upperCase(key));
                            });

            System.out.println(">>>>>>> Secret key is coming soon ......");

            // When
            secretKeyPrintFuture.join();
        }

        @Test
        void test_Processing_Results_With_thenRun_Runnable() {

            // Given
            CompletableFuture<Void> secretKeyPrintFuture =
                    CompletableFuture.supplyAsync(
                            () -> RandomStringUtils.randomAlphanumeric(10))
                            .thenRun(() -> System.out.println(
                                    ">>>>>>> Key generated!"));

            // When
            secretKeyPrintFuture.join();
        }

    }//: End of class ProcessingResultsOfAsyncComputationsTest

    @Nested
    @DisplayName("Test Processing Results of Asynchronous Computations - ")
    class CombiningFuturesTest {



    }//: End of class CombiningFuturesTest

}///:~