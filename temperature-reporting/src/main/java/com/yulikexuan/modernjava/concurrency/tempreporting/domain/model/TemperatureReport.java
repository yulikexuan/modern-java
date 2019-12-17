//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.TemperatureReport.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.services.ITempInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.concurrent.Flow.*;


@Component
public class TemperatureReport implements ITemperatureReport {

    private String town;
    private final ITempInfoService tempInfoService;
    private final TempSubscriber subscriber;

    @Autowired
    public TemperatureReport(ITempInfoService tempInfoService,
                             TempSubscriber subscriber) {
        this.tempInfoService = tempInfoService;
        this.subscriber = subscriber;
    }

    @Override
    public void reportTemperatureForTown(String town) {
        this.town = town;
        this.subscribe(this.subscriber);
    }

    @Override
    public void subscribe(Subscriber<? super ITempInfo> subscriber) {
        TempSubscription tempSubscription = TempSubscription.newSubscription(
                town, subscriber, tempInfoService);
        subscriber.onSubscribe(tempSubscription);
    }

}///:~