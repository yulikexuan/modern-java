//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.TempProcessor.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


import org.springframework.stereotype.Component;

import static java.util.concurrent.Flow.*;


@Component
public class TempProcessor implements Processor<ITempInfo, ITempInfo> {

    private Subscriber<? super ITempInfo> subscriber;

    @Override
    public void subscribe(Subscriber<? super ITempInfo> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(ITempInfo tempInfo) {
        this.subscriber.onNext(ITempInfo.convertToCelsius(tempInfo));
    }

    @Override
    public void onError(Throwable throwable) {
        this.subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        this.subscriber.onComplete();
    }

}///:~