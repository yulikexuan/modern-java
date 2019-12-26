//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.rx.TemperaturePublisher.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.rx;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo;
import com.yulikexuan.modernjava.concurrency.tempreporting.domain.services.ITempInfoService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
public class TemperaturePublisher implements ITemperaturePublisher {

    private final Observable<Long> onePerSec;
    private final ITempInfoService tempInfoService;
    private final Observer<ITempInfo> tempObserver;

    @Autowired
    public TemperaturePublisher(ITempInfoService tempInfoService,
                                Observer<ITempInfo> tempObserver) {

        this.onePerSec = Observable.interval(1, TimeUnit.SECONDS);
        this.tempInfoService = tempInfoService;
        this.tempObserver = tempObserver;
    }

    @Override
    public void reportTemperatureForTown(String... towns) {

        List<Observable<ITempInfo>> observables = Arrays.stream(towns)
                .map(town -> this.generalizedReporter(town))
                .map(o -> o.map(t -> ITempInfo.convertToCelsius(t)))
                .map(o -> o.filter(t -> t.getTemp() < 0))
                .collect(Collectors.toList());

        Observable<ITempInfo> globalObservable = Observable.merge(observables);

        globalObservable.blockingSubscribe(this.tempObserver);
    }

    private Observable<ITempInfo> generalizedReporter(String town) {
        return Observable.create(
                emitter -> this.onePerSec.subscribe(i -> {
                    if (!emitter.isDisposed()) {
                        if (i > 15) {
                            emitter.onComplete();
                        } else {
                            try {
                                emitter.onNext(this.tempInfoService.fetch(town));
                            } catch (Throwable e) {
                                emitter.onError(e);
                            }
                        }
                    }
                }));
    }

}///:~