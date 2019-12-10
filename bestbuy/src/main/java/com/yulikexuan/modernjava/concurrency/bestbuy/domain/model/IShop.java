//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.IShop.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


public interface IShop {

    int LONG_PROCESSING_TIME = 1000;

    String getName();

    double getPrice(String product);

    static void delay() {
        try {
            Thread.sleep(LONG_PROCESSING_TIME);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

}///:~