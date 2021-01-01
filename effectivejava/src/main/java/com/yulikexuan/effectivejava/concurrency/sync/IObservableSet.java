//: com.yulikexuan.effectivejava.concurrency.sync.IObservableSet.java

package com.yulikexuan.effectivejava.concurrency.sync;


import java.util.Set;


public interface IObservableSet<E> extends Set<E> {

    void addObserver(SetObserver<E> observer);
    boolean removeObserver(SetObserver<E> observer);

}///:~