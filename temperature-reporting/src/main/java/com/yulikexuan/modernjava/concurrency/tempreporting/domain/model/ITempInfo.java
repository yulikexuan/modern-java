//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


public interface ITempInfo {

    String REPORT_TEMPLATE = "-------> %-20s : %4d (F)";

    int getTemp();
    String getTown();
    String toString();

}///:~