//: com.yulikexuan.modernjava.lambdas.observer.IObserver.java


package com.yulikexuan.modernjava.lambdas.observer;


import java.util.Optional;


@FunctionalInterface
public interface IObserver {

    Optional<String> notify(String tweet);

}///:~