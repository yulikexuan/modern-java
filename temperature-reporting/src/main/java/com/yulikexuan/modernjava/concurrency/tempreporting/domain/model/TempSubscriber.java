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
    @Override
    public void onComplete() {
        System.out.println(">>>>>>> Temperature Reporting is Done! <<<<<<<");
    }

}///:~