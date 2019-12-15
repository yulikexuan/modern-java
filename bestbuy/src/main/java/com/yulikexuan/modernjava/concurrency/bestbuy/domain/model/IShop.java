//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.IShop.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import javax.money.CurrencyUnit;


public interface IShop {

    int LONG_PROCESSING_TIME = 1000;

    String getName();
    CurrencyUnit getCurrencyUnit();

    double getPrice(String product);

    static void delay() {
        try {
            Thread.sleep(LONG_PROCESSING_TIME);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

}///:~