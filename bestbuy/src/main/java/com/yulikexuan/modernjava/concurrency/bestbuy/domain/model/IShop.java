//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.IShop.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import javax.money.CurrencyUnit;
import java.util.concurrent.ThreadLocalRandom;


public interface IShop {

    long MINIMUM_PROCESSING_TIME = 300L;
    long MAXIMUM_PROCESSING_TIME = 1000L;

    String getName();
    CurrencyUnit getCurrencyUnit();

    double getPrice(String product);

    static void delay() {
        long delay = ThreadLocalRandom.current().nextLong(
                MINIMUM_PROCESSING_TIME,
                MAXIMUM_PROCESSING_TIME);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

}///:~