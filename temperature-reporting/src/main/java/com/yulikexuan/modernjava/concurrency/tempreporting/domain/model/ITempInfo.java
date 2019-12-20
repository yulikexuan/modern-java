//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


public interface ITempInfo {

    String REPORT_TEMPLATE = "-------> %-12s : %4d (F)";

    int getTemp();
    String getTown();
    String toString();

    static ITempInfo convertToCelsius(ITempInfo fahrenheit) {
        return TempInfo.builder().town(fahrenheit.getTown())
                .temp((fahrenheit.getTemp() -32) * 5 / 9)
                .build();
    }

}///:~