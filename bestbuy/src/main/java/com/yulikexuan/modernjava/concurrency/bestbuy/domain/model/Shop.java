//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.Shop.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;


@Data
@NoArgsConstructor
@Builder @AllArgsConstructor
public class Shop implements IShop {

    private String name;

    @Override
    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                /* If the price calculation completed normally, complete the
                 * Future with the price.
                 */
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Throwable e) {
                /* Otherwise, complete the Future exceptionally with the
                 * Exception that caused the failure.
                 */
                futurePrice.completeExceptionally(e);
            }
        }).start();
        return futurePrice;
    }

    public Future<Double> getPriceAsyncApi(final String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    private double calculatePrice(String product) {
        if (product == null) {
            throw new ProductNotFountException();
        }
        IShop.delay();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

}///:~