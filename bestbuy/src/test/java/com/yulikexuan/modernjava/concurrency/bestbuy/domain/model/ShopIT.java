//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.ShopIT.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import static com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.IShop.MINIMUM_PROCESSING_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ShopIT {

    private String product;
    private Shop shop;

    private Instant start;
    private Long invocationTime;
    private Long retrievalTime;

    @BeforeEach
    void setUp() {
        this.shop = Shop.builder().name("bestbuy").build();
        this.product = "F6F Model";
    }

    @Nested
    @DisplayName("Implemented asynchronous Api Test - ")
    class AsynchronousApiTest {

        @Test
        @DisplayName("Test using CompletableFuture as a normal Future mannually - ")
        void test_Converting_Sync_Method_Into_Async_One_Mannually() {

            // Given

            // When & Then
            this.testAsyncImplementations(product, shop::getPriceAsync);
        }

        @Test
        @DisplayName("Test using CompletableFuture as a normal Future - ")
        void test_Converting_Sync_Method_Into_Async_One() {

            // Given

            // When & Then
            this.testAsyncImplementations(product, shop::getPriceAsyncApi);
        }

        private void testAsyncImplementations(
                String product,
                Function<String, Future<Double>> gettingPrice) {

            // Given

            // When
            start = Instant.now();
            Future<Double> futurePrice = gettingPrice.apply(product);
            invocationTime = Duration.between(start, Instant.now()).toMillis();

            double price = -1;
            try {
                price = futurePrice.get(2000, TimeUnit.MILLISECONDS);
            } catch (TimeoutException |
                    InterruptedException |
                    ExecutionException e) {

                throw new RuntimeException(e);
            }

            retrievalTime = Duration.between(start, Instant.now()).toMillis();

            // Then
            assertThat(retrievalTime)
                    .as("The retrieval time should be greater than " +
                            "the processing time.")
                    .isBetween(MINIMUM_PROCESSING_TIME, IShop.MAXIMUM_PROCESSING_TIME + MINIMUM_PROCESSING_TIME);
            assertThat(retrievalTime).isGreaterThan(invocationTime);
            assertThat(invocationTime).isLessThan(3);
        }

        @Test
        @DisplayName("Test dealing with errors from CompletableFuture mannually- ")
        void test_Dealing_With_Errors_From_CompletableFuture_Manually() {

            // When
            testDealingWithErrorsFromCompletableFuture(shop::getPriceAsync);
        }

        @Test
        @DisplayName("Test dealing with errors from CompletableFuture - ")
        void test_Dealing_With_Errors_From_CompletableFuture() {

            // When
            testDealingWithErrorsFromCompletableFuture(shop::getPriceAsyncApi);
        }

        void testDealingWithErrorsFromCompletableFuture(
                Function<String, Future<Double>> gettingPrice) {

            // When
            Future<Double> futurePrice = gettingPrice.apply(null);

            // Then
            assertThatThrownBy(() -> futurePrice.get())
                    .as("Should not have any price for null product")
                    .isInstanceOf(ExecutionException.class)
                    .hasCauseExactlyInstanceOf(ProductNotFountException.class);
        }

    } //: class AsynchronousApiTest

}///:~