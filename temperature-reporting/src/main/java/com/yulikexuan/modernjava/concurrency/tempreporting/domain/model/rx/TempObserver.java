//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.rx.TempObserver.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.rx;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.springframework.stereotype.Component;


@Component
public class TempObserver implements Observer<ITempInfo> {

    @Override
    public void onSubscribe(Disposable d) {
        // TODO
    }

    @Override
    public void onNext(ITempInfo tempInfo) {
        System.out.println(tempInfo);
    }

    @Override
    public void onError(Throwable e) {
        System.out.println("--- XXX Got Problem ! XXX --- ");
        System.out.println(e.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("--- !!! Job Done !!! --- ");
    }

}///:~