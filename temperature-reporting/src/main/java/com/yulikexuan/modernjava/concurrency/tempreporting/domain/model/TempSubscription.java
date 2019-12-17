//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.TempSubscription.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.services.ITempInfoService;

import java.util.stream.LongStream;

import static java.util.concurrent.Flow.Subscriber;
import static java.util.concurrent.Flow.Subscription;


public class TempSubscription implements Subscription {

    private final String town;
    private final Subscriber<? super ITempInfo> subscriber;
    private final ITempInfoService tempInfoService;

    private TempSubscription(
            String town, Subscriber<? super ITempInfo> subscriber,
            ITempInfoService tempInfoService) {

        this.town = town;
        this.subscriber = subscriber;
        this.tempInfoService = tempInfoService;
    }

    public static TempSubscription newSubscription(
            String town, Subscriber<? super ITempInfo> subscriber,
            ITempInfoService tempInfoService) {

        return new TempSubscription(town, subscriber, tempInfoService);
    }

    @Override
    public void request(long n) {
        LongStream.range(0L, n)
                .forEach(l -> {
                    try {
                        // Method invoked with a Subscription's next item
                        this.subscriber.onNext(this.tempInfoService.fetch(town));
                    } catch (Exception e) {
                        // If Subscriber::onNext throws an exception,
                        // resulting behavior is not guaranteed
                        // but may cause the Subscription to be cancelled
                        this.subscriber.onError(e);
                    }
                });
    }

    /*
     * If the subscription is canceled, send a completion (onComplete) signal
     * to the Subscriber
     */
    @Override
    public void cancel() {
        // Subscriber::
        //     onSubscribe --> onNext* --> ( onError | onComplete)
        // Subscriber::onComplete : the stream of events can go on forever,
        //                          or it can be terminated by an onComplete
        //                          callback to signify that no more elements
        //                          will be produced
        // Method invoked when it is known that no additional Subscriber method
        // invocations will occur for a Subscription that is not already
        // terminated by error, after which no other Subscriber methods are
        // invoked by the Subscription
        this.subscriber.onComplete();
    }

}///:~