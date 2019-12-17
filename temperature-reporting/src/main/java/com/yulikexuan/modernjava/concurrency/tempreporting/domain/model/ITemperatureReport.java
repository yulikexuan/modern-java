//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITemperatureReport.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


import static java.util.concurrent.Flow.*;


public interface ITemperatureReport extends Publisher<ITempInfo> {

    void reportTemperatureForTown(String town);

}///:~