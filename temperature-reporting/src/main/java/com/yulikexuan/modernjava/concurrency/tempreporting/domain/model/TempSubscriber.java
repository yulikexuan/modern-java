//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.TempSubscriber.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


import org.springframework.stereotype.Component;

import static java.util.concurrent.Flow.*;


@Component
public class TempSubscriber implements Subscriber<ITempInfo> {

    private Subscription subscription;

    /*
     * Stores the subscription and sends a first request
     */
    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    /*
     * Prints the received temperature and requests a further one
     */
    @Override
    public void onNext(ITempInfo tempInfo) {
        System.out.println(tempInfo);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(">>>>>>> STOPED! <<<<<<<");
    }

    @Override
    public void onComplete() {
        System.out.println(">>>>>>> Temperature Reporting is Done! <<<<<<<");
    }

}///:~