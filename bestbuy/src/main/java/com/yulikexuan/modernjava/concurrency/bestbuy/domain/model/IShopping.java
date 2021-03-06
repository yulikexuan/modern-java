//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.IShopping.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import com.google.common.collect.ImmutableList;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;


public interface IShopping {

    String[] SHOP_NAMES = {
            " 1 - BestPrice",
            " 2 - LetsSaveBig",
            " 3 - MyFavoriteShop",
            " 4 - BuyItAll",
            " 5 - Amazon",
            " 6 - Costsco",
            " 7 - Walmart",
            " 8 - BestBuy",
            " 9 - FutureShop",
            "10 - WIX",
            "11 - Volusion",
            "12 - Squarespace"
    };

    CurrencyUnit[] CURRENCY_UNITS = {
            Monetary.getCurrency("USD"),
            Monetary.getCurrency("CAD"),
            Monetary.getCurrency("EUR"),
            Monetary.getCurrency("GBP")
    };

    // Set an upper limit of 100 threads to avoid a server crash for a larger
    // number of shops
    int MAXIMUM_THREADS_NUMBER = 100;
    int MINIMUM_THREADS_NUMBER = SHOP_NAMES.length * 2;
    int THE_BEST_THREADS_NUMBER = Math.min(IShopping.MINIMUM_THREADS_NUMBER,
            IShopping.MAXIMUM_THREADS_NUMBER);
    int DEFAULT_TIME_OUT_IN_SECOND = 3;

    ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    List<IShop> SHOPS = Arrays.stream(SHOP_NAMES)
            .map(name -> Shop.builder()
                    .name(name)
                    .currencyUnit(CURRENCY_UNITS[RANDOM.
                            nextInt(0, CURRENCY_UNITS.length)])
                    .build())
            .collect(ImmutableList.toImmutableList());

    List<String> findPricesSequentially(String product);

    List<String> findPricesParallelizely(String product);

    List<String> findPricesAsynchronously(String product);

    List<String> findPricesAsynchronouslyWithCustomExecutor(String product);

    List<String> findPromotionSynchronously(final String product);

    List<String> findPromotionAsync(final String product);

    List<String> findPromotionQuoteAsync(final String product);

    void printPromotionQuoteReactively(final String product);

}///:~