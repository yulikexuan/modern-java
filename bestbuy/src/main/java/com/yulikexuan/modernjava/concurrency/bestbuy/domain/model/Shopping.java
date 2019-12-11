//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.Shopping.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


/*
 * Parallelism: via Streams or CompletableFutures?
 *
 * 1. If doing computation-heavy operations with no I/O, the Stream interface
 *    provides the simplest implementation and the one that’s likely to be most
 *    efficient
 *
 *      - If all threads are compute-bound, there’s no point in having more
 *        threads than processor cores
 *      - In computer science, a computer is CPU-bound (or compute-bound) when
 *        the time for it to complete a task is determined principally by the
 *        speed of the central processor:
 *
 *          - processor utilization is high, perhaps at 100% usage for many
 *            seconds or minutes
 *
 * 2. If the parallel units of work involve waiting for I/O (including network
 *    connections), the CompletableFutures solution provides more flexibility
 *    and allows you to match the number of threads to the wait/computer (W/C)
 *    ratio, as discussed previously
 *
 *    Another reason to avoid using parallel streams when I/O waits are involved
 *    in the stream-processing pipeline is that the laziness of streams can make
 *    it harder to reason about when the waits happen
 *
 *    Nthreads = NCPU * UCPU * (1 + W/C)
 *
 *      - NCPU is the number of cores, available through
 *        Runtime.getRuntime().availableProcessors()
 *
 *      - UCPU is the target CPU use (between 0 and 1).
 *
 *      - W/C is the ratio of wait time to compute time
 *
 *      - Use daemon threads, which don’t prevent the termination of the program
 *
 *      - Create a thread pool with a number of threads equal to the minimum of
 *        100 and the number of elements of the collection or the map
 */
public class Shopping implements IShopping {

    private final Executor executor;
    private final ThreadLocalRandom random;

    public Shopping(Executor executor) {
        this.executor = executor;
        this.random = ThreadLocalRandom.current();
    }

    @Override
    public List<String> findPricesSequentially(String product) {
        return SHOPS.stream()
                .map(shop -> getPriceSynchronously(shop, product))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<String> findPricesParallelizely(String product) {
        return SHOPS.stream().parallel()
                .map(shop -> getPriceSynchronously(shop, product))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<String> findPricesAsynchronously(String product) {

        /*
         * Gathering the CompletableFutures in a list allows all of them to
         * start before waiting for their completion
         */
        List<CompletableFuture<String>> priceFutures = SHOPS.stream()
                .map(shop -> getPriceAsynchronously(shop, product))
                .collect(ImmutableList.toImmutableList());

        return priceFutures.stream()
                /*
                 * Waiting for the completion of all asynchronous operations
                 *
                 * The join() method doesn't throw checked exceptions.
                 *
                 * The CompletableFuture.join() method is similar to the get
                 * method, but it throws an unchecked exception in case the
                 * Future does not complete normally. This makes it possible to
                 * use it as a method reference in the Stream.map() method
                 */
                .map(CompletableFuture::join)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<String> findPricesAsynchronouslyWithCustomExecutor(
            String product) {

        List<CompletableFuture<String>> priceFutures = SHOPS.stream()
                .map(shop -> getPriceAsyncWithCustomExecutor(
                        shop, product))
                .collect(ImmutableList.toImmutableList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<String> findPromotionSynchronously(final String product) {

        return SHOPS.stream()
                .map(shop -> getPriceSynchronously(shop, product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<String> findPromotionAsync(final String product) {

        List<CompletableFuture<String>> promotionFutures = SHOPS.stream()
                .map(shop -> getPriceAsyncWithCustomExecutor(shop, product))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(
                        quote -> CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote),
                                this.executor)))
                .collect(ImmutableList.toImmutableList());

        return promotionFutures.stream()
                .map(CompletableFuture::join)
                .collect(ImmutableList.toImmutableList());
    }

    private String getPriceSynchronously(IShop shop, String product) {
        double price = shop.getPrice(product);
        Discount.Code[] allCodes = Discount.Code.values();
        Discount.Code discountCode = allCodes[this.random.current()
                .nextInt(0, allCodes.length)];
        return String.format("%s:%.2f:%s", shop.getName(),
                shop.getPrice(product), discountCode);
    }

    private CompletableFuture<String> getPriceAsynchronously(
            IShop shop, String product) {

        return CompletableFuture.supplyAsync(
                () -> getPriceSynchronously(shop, product));
    }

    /*
     * This example demonstrates the fact that it’s a good idea to create an
     * Executor that fits the characteristics of your application and to use
     * CompletableFutures to submit tasks to it.
     *
     * This strategy is almost always EFFECTIVE and something to consider when
     * you make intensive use of asynchronous operations.
     */
    private CompletableFuture<String> getPriceAsyncWithCustomExecutor(
            IShop shop, String product) {

        return CompletableFuture.supplyAsync(
                () -> getPriceSynchronously(shop, product),
                this.executor);
    }

}///:~