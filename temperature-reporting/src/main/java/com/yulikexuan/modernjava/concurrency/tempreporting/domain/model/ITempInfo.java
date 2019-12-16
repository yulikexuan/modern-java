//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


import java.util.concurrent.ThreadLocalRandom;


public interface ITempInfo {

    ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    int getTemp();
    String getTown();
    String toString();

}///:~