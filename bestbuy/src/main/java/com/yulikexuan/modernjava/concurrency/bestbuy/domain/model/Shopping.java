//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.Shopping.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class Shopping implements IShopping {

    private final Executor executor;

    public Shopping(Executor executor) {
        this.executor = executor;
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
                .map(shop -> getPriceAsynchronouslyWithCustomExecutor(
                        shop, product))
                .collect(ImmutableList.toImmutableList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(ImmutableList.toImmutableList());
    }

    private String getPriceSynchronously(IShop shop, String product) {
        return String.format("%1$s's price is $%2$.2f", shop.getName(),
                shop.getPrice(product));
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
    private CompletableFuture<String> getPriceAsynchronouslyWithCustomExecutor(
            IShop shop, String product) {

        return CompletableFuture.supplyAsync(
                () -> getPriceSynchronously(shop, product),
                this.executor);
    }

}///:~