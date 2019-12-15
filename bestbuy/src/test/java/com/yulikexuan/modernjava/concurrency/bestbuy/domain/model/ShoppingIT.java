//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.ShoppingIT.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import com.yulikexuan.modernjava.concurrency.bestbuy.domain.services.ExchangeService;
import com.yulikexuan.modernjava.concurrency.bestbuy.domain.services.IExchangeService;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShoppingIT {

    static final String FAVORITE_PRODUCT_NAME = "F6F Model";
    static final String DURATION_OF_FINDING_PRICES_TEMPLATE =
            ">>>>>>> The duration of finding all prices %1$s is %2$d msec.%n";
    static final String DURATION_OF_FINDING_PROMOTIONS_TEMPLATE =
            ">>>>>>> The duration of finding all promotions %1$s is %2$d msec.%n";

    private IExchangeService exchangeService;
    private ExecutorService executor;
    private Shopping shopping;

    private Instant start;
    private long duration;

    @BeforeEach
    void setUp() {
        this.exchangeService = new ExchangeService();
        this.executor = Executors.newFixedThreadPool(
                IShopping.THE_BEST_THREADS_NUMBER,
                runnable -> {
                    Thread thread = new Thread(runnable);
                    thread.setDaemon(true);
                    return thread;
                });

        this.shopping = new Shopping(this.executor, this.exchangeService);
    }

    @AfterEach
    void tearDown() {
        // Reject incoming tasks
        this.executor.shutdown();
        try {
            if (!this.executor.awaitTermination(
                    1000, TimeUnit.MILLISECONDS)) {
                // Cancel any lingering tasks
                this.executor.shutdownNow();
                if (!this.executor.awaitTermination(
                        1000, TimeUnit.MILLISECONDS)) {
                    System.out.println(
                            "XXXXXXX Thread Executor did not terminate XXXXXXX");
                }
            }
        } catch (InterruptedException e) {
            // (Re-)Cancel if current thread also interrupted
            this.executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    @Disabled
    @Test
    @DisplayName("Test the performance of sequentially finding prices - ")
    void testFindingPricesSequentially() {

        // Given
        start = Instant.now();

        // When
        List<String> priceList = this.shopping.findPricesSequentially(
                FAVORITE_PRODUCT_NAME);

        duration = Duration.between(start, Instant.now()).toMillis();

        // Then
        assertThat(priceList.size()).isEqualTo(IShopping.SHOPS.size());
        assertThat(duration).isGreaterThan(
                IShop.LONG_PROCESSING_TIME * IShopping.SHOPS.size());
    }

    @Test
    @Order(1)
    @DisplayName("Test the performance of parallelizely finding prices - ")
    void testFindingPricesParallelizely() {

        // Given
        start = Instant.now();

        // When
        List<String> priceList = this.shopping.findPricesParallelizely(
                FAVORITE_PRODUCT_NAME);

        duration = Duration.between(start, Instant.now()).toMillis();
        System.out.printf(DURATION_OF_FINDING_PRICES_TEMPLATE,
                "parallelizely", duration);

        // Then
        assertThat(priceList.size()).isEqualTo(IShopping.SHOPS.size());
        assertThat(duration).isLessThan(IShop.LONG_PROCESSING_TIME * 4 + 100);
    }

    @Test
    @Order(2)
    @DisplayName("Test the performance of asynchronously finding prices - ")
    void testFindingPricesAsynchronously() {

        // Given
        start = Instant.now();

        // When
        List<String> priceList = this.shopping.findPricesAsynchronously(
                FAVORITE_PRODUCT_NAME);

        duration = Duration.between(start, Instant.now()).toMillis();
        System.out.printf(DURATION_OF_FINDING_PRICES_TEMPLATE,
                "asynchronously", duration);

        // Then
        assertThat(priceList.size()).isEqualTo(IShopping.SHOPS.size());
        assertThat(duration).isLessThan(4 * IShop.LONG_PROCESSING_TIME + 100);
    }

    @Test
    @Order(3)
    @DisplayName("Test the performance of async finding with custom Executor - ")
    void testFindingPricesAsynchronouslyWithCustomExecutor() {
        // Given
        start = Instant.now();

        // When
        List<String> priceList =
                this.shopping.findPricesAsynchronouslyWithCustomExecutor(
                        FAVORITE_PRODUCT_NAME);

        duration = Duration.between(start, Instant.now()).toMillis();
        System.out.printf(DURATION_OF_FINDING_PRICES_TEMPLATE,
                "async with custom Executor", duration);

        // Then
        assertThat(priceList.size()).isEqualTo(IShopping.SHOPS.size());
        assertThat(duration).isLessThan(IShop.LONG_PROCESSING_TIME * 2 + 100);
    }

    @Disabled
    @Test
    @DisplayName("Test the synchronous promotion pipeline - ")
    void testSynchronusPromotionPipeline() {

        // Given
        start = Instant.now();

        // When
        List<String> promotions = this.shopping.findPromotionSynchronously(
                FAVORITE_PRODUCT_NAME);

        duration = Duration.between(start, Instant.now()).toMillis();

        // Then
        System.out.printf(DURATION_OF_FINDING_PROMOTIONS_TEMPLATE,
                "synchronously", duration);
        assertThat(promotions.size()).isEqualTo(IShopping.SHOPS.size());
        System.out.println(promotions);
    }

    @Test
    @DisplayName("Test the Asynchronous promotion pipeline - ")
    void testAsyncPromotionPipeline() {

        // Given
        start = Instant.now();

        // When
        List<String> promotions = this.shopping.findPromotionAsync(
                FAVORITE_PRODUCT_NAME);

        duration = Duration.between(start, Instant.now()).toMillis();

        // Then
        System.out.printf(DURATION_OF_FINDING_PROMOTIONS_TEMPLATE,
                "asynchronously", duration);
        assertThat(promotions.size()).isEqualTo(IShopping.SHOPS.size());
        assertThat(duration).isLessThan(IShop.LONG_PROCESSING_TIME * 3 + 200);
    }

    @Test
    @DisplayName("Test compose two combined CompletableFutures - ")
    void testComposeTwoCombinedCompletableFuture() {

        // Given
        start = Instant.now();

        // When
        List<String> promotions = shopping.findPromotionQuoteAsync(
                FAVORITE_PRODUCT_NAME);

        duration = Duration.between(start, Instant.now()).toMillis();

        // Then
        System.out.printf(DURATION_OF_FINDING_PROMOTIONS_TEMPLATE,
                "by composing two combined Futures", duration);
        assertThat(promotions.size()).isEqualTo(IShopping.SHOPS.size());
        assertThat(duration).isLessThan(IShop.LONG_PROCESSING_TIME * 2 + 200);
    }

}///:~