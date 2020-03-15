//: com.yulikexuan.modernjava.concurrency.asyncapi.CompletableFutureTest.java


package com.yulikexuan.modernjava.concurrency.asyncapi;


import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Stream;

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
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

            // When & Then
            assertTimeout(Duration.ofMillis(600), () -> completableFuture.join());
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

        private long theFirstFutureThreadId;
        private long theSecondFutureThreadId;

        private boolean isRunning;

        void setUp() {
            this.isRunning = false;
        }

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
        void test_Processing_Results_With_thenApply_Function() {

            // Given
            CompletableFuture<String> secretKeyFuture =
                    CompletableFuture.supplyAsync(() -> {
                            theFirstFutureThreadId =
                                    Thread.currentThread().getId();
                            return RandomStringUtils.randomAlphanumeric(10);
                    });

            CompletableFuture<String> finalKeyFuture =
                    secretKeyFuture.thenApply(key -> {
                            theSecondFutureThreadId =
                                    Thread.currentThread().getId();
                            sleep(200);
                            return StringUtils.upperCase(key);
                    });

            // When
            /*
             * The CompletableFuture.join() method is similar to the get method,
             * but it throws an unchecked exception in case the Future does not
             * complete normally.
             * This makes it possible to use it as a method reference in the
             * Stream.map() method.
             */
            String fullSecretKey = secretKeyFuture.join();

            // Then
            assertThat(fullSecretKey.length()).isEqualTo(10);
            assertThat(StringUtils.isAlphanumeric(fullSecretKey)).isTrue();
        }

        @Test
        void test_Processing_Results_With_thenApplyAsync_Function() {
            // Given
            CompletableFuture<String> secretKeyFuture =
                    CompletableFuture.supplyAsync(() -> {
                        theFirstFutureThreadId =
                                Thread.currentThread().getId();
                        return RandomStringUtils.randomAlphanumeric(10);
                    });

            CompletableFuture<String> finalKeyFuture =
                    secretKeyFuture.thenApplyAsync(key -> {
                        theSecondFutureThreadId =
                                Thread.currentThread().getId();
                        sleep(200);
                        return StringUtils.upperCase(key);
                    });

            // When & Then
            assertThat(this.theFirstFutureThreadId).isNotEqualTo(
                    this.theSecondFutureThreadId);
        }

        @Test
        void test_Processing_Results_With_thenAcceptAsync_Consumer() {

            // Given
            CompletableFuture<Void> secretKeyPrintFuture =
                    CompletableFuture.supplyAsync(
                            () -> RandomStringUtils.randomAlphanumeric(10))
                            .thenAcceptAsync(key -> {
                                sleep(200);
                            });

            // When
            secretKeyPrintFuture.join();
        }

        @Test
        void test_Processing_Results_With_thenRun_Runnable() {

            // Given
            CompletableFuture<Void> secretKeyPrintFuture =
                    CompletableFuture.supplyAsync(
                            () -> RandomStringUtils.randomAlphanumeric(10))
                            .thenRun(() -> this.isRunning = true);

            // When
            secretKeyPrintFuture.join();

            // Then
            assertThat(this.isRunning).isTrue();
        }

    }//: End of class ProcessingResultsOfAsyncComputationsTest

    @Nested
    @DisplayName("Test Processing Results of Asynchronous Computations - ")
    class CombiningFuturesTest {

        private long theMainThreadId;
        private long theFirstFutureThreadId;
        private long theSecondFutureThreadId;
        private long theFinalThreadId;

        /*
         * CompletableFuture::thenCompose is like Stream::flatMap
         */
        @Test
        void test_FlatMap_Method_For_Stream() {

            // Given
            String[] testData = {
                    "The difference between thenApply and thenCompose.",
                    "CompletableFuture is the core of asynchronous programming."
            };

            // When
            List<String> units = Stream.of(testData)
                    .map(data -> data.split(""))
                    .flatMap(Arrays::stream)
                    .distinct()
                    .collect(ImmutableList.toImmutableList());

            // Then
        }

        /*
         * The method CompletableFuture::thenCompose takes a function that
         * returns a CompletableFuture instance
         *
         * The argument of this function is the result of the previous
         * computation step allowing to use this value inside the next
         * CompletableFuture‘s lambda
         *
         * The thenCompose method together with thenApply implement basic
         * building blocks of the monadic pattern
         *
         * They closely relate to the map and flatMap methods of Stream and
         * Optional classes also available in Java 8
         *
         * Both methods receive a function and apply it to the computation
         * result, but the thenCompose (flatMap) method receives a function
         * that returns another object of CompletableFuture
         *
         * This functional structure allows composing the instances of these
         * classes as building blocks
         */
        @Test
        void test_Combining_Futures_With_thenCompose() {

            // Given
            this.theMainThreadId = Thread.currentThread().getId();

            CompletableFuture<String> completableFuture_1 =
                    CompletableFuture.supplyAsync(() -> {
                            this.theFirstFutureThreadId =
                                    Thread.currentThread().getId();
                            return "Hello";
                    });

            Function<String, CompletableFuture<String>> composeFunc =
                    data -> CompletableFuture.supplyAsync(() -> {
                            this.theSecondFutureThreadId =
                                    Thread.currentThread().getId();
                            return data + " Asynchronous Programming!";
                    });

            CompletableFuture<String> completableFuture_2 =
                    completableFuture_1.thenCompose(composeFunc);

            // When
            String result = completableFuture_2.join();

            // Then
            assertThat(result).isEqualTo("Hello Asynchronous Programming!");
            assertThat(this.theFirstFutureThreadId).isNotEqualTo(
                    this.theMainThreadId);
            assertThat(this.theSecondFutureThreadId).isNotEqualTo(
                    this.theMainThreadId);
        }

        @Test
        void test_Combining_Two_Independent_Futures_With_thenCombine() {

            // Given
            this.theMainThreadId = Thread.currentThread().getId();

            CompletableFuture<String> completableFuture_1 =
                    CompletableFuture.supplyAsync(() -> {
                        this.theFirstFutureThreadId =
                                Thread.currentThread().getId();
                        return "Hello";
                    });

            CompletableFuture<String> completableFuture_2 =
                    CompletableFuture.supplyAsync(() -> {
                        this.theSecondFutureThreadId =
                                Thread.currentThread().getId();
                        return " Asynchronous Programming!";
                    });

            CompletableFuture<String> completableFuture_3 =
                    completableFuture_1.thenCombine(completableFuture_2,
                            (s1, s2) -> {
                                    this.theFinalThreadId =
                                            Thread.currentThread().getId();
                                    return String.join("", s1, s2);
                            });

            // When
            String result = completableFuture_3.join();

            // Then
            assertThat(this.theMainThreadId).isEqualTo(this.theFinalThreadId);
        }

        @Test
        void test_Combining_Two_Independent_Futures_With_thenAcceptBoth() {

            // Given
            this.theMainThreadId = Thread.currentThread().getId();

            CompletableFuture<String> completableFuture_1 =
                    CompletableFuture.supplyAsync(() -> {
                        this.theFirstFutureThreadId =
                                Thread.currentThread().getId();
                        return "Hello";
                    });

            CompletableFuture<String> completableFuture_2 =
                    CompletableFuture.supplyAsync(() -> {
                        this.theSecondFutureThreadId =
                                Thread.currentThread().getId();
                        return " Asynchronous Programming!";
                    });

            CompletableFuture<Void> completableFuture_3 =
                    completableFuture_1.thenAcceptBoth(completableFuture_2,
                            (s1, s2) -> {
                                this.theFinalThreadId =
                                        Thread.currentThread().getId();
                            });

            // When
            completableFuture_3.join();

            // Then
            assertThat(this.theMainThreadId).isEqualTo(this.theFinalThreadId);
        }

    }//: End of class CombiningFuturesTest

}///:~