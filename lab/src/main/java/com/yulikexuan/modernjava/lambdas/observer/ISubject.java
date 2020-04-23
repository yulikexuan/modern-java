//: com.yulikexuan.modernjava.lambdas.observer.ISubject.java


package com.yulikexuan.modernjava.lambdas.observer;


import java.util.List;


public interface ISubject {

    boolean registerObserver(IObserver observer);
    List<String> notifyObservers(String tweet);

    void clearObservers();

}///:~