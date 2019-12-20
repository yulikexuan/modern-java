//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.CelsiusTemperatureReport.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.services.ITempInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.concurrent.Flow.Processor;
import static java.util.concurrent.Flow.Subscriber;


@Component()
public class CelsiusTemperatureReport implements ITemperatureReport {

    private final ITempInfoService tempInfoService;
    private final TempSubscriber subscriber;

    private String town;
    private Processor<ITempInfo, ITempInfo> processor;

    @Autowired
    public CelsiusTemperatureReport(ITempInfoService tempInfoService,
                             TempSubscriber subscriber) {
        this.tempInfoService = tempInfoService;
        this.subscriber = subscriber;
    }

    @Override
    public void reportTemperatureForTown(String town) {
        this.town = town;
        this.processor = new TempProcessor();
        this.subscribe(this.subscriber);
    }

    @Override
    public void subscribe(Subscriber<? super ITempInfo> subscriber) {
        this.processor.subscribe(subscriber);
        TempSubscription tempSubscription = TempSubscription.newSubscription(
                town, processor, tempInfoService);
        this.processor.onSubscribe(tempSubscription);
    }

}///:~