//: com.yulikexuan.modernjava.lambdas.observer.Feed.java


package com.yulikexuan.modernjava.lambdas.observer;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;


public class Feed implements ISubject {

    private final List<IObserver> observers = Lists.newArrayList();

    @Override
    public boolean registerObserver(IObserver observer) {
        return this.observers.add(observer);
    }

    @Override
    public List<String> notifyObservers(String tweet) {
        return this.observers.stream()
                .map(observer -> observer.notify(tweet))
                .filter(opt -> opt.isPresent())
                .map(opt -> opt.get())
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public void clearObservers() {
        this.observers.clear();
    }

}///:~