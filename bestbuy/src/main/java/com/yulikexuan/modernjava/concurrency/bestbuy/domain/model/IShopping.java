//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.IShopping.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;


public interface IShopping {

    String[] SHOP_NAMES = {
            "BestPrice",
            "LetsSaveBig",
            "MyFavoriteShop",
            "BuyItAll",
            "Amazon",
            "Costsco",
            "Walmart",
            "BestBuy",
            "FutureShop",
            "WIX",
            "Volusion",
            "Squarespace"
    };

    // Set an upper limit of 100 threads to avoid a server crash for a larger
    // number of shops
    int MAXIMUM_THREADS_NUMBER = 100;
    int MINIMUM_THREADS_NUMBER = SHOP_NAMES.length;
    int THE_BEST_THREADS_NUMBER = Math.min(IShopping.MINIMUM_THREADS_NUMBER,
            IShopping.MAXIMUM_THREADS_NUMBER);

    List<IShop> SHOPS = Arrays.stream(SHOP_NAMES)
            .map(name -> Shop.builder().name(name).build())
            .collect(ImmutableList.toImmutableList());

    List<String> findPricesSequentially(String product);

    List<String> findPricesParallelizely(String product);

    List<String> findPricesAsynchronously(String product);

    List<String> findPricesAsynchronouslyWithCustomExecutor(String product);

    List<String> findPromotionSynchronously(final String product);

    List<String> findPromotionAsync(final String product);

}///:~