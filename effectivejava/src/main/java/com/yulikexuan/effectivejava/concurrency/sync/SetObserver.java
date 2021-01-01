//: com.yulikexuan.effectivejava.concurrency.sync.SetObserver.java

package com.yulikexuan.effectivejava.concurrency.sync;


// Set obeserver callback interface - Page 266
@FunctionalInterface
public interface SetObserver<E> {

    // Invoked when an element is added to the observable set
    void added(IObservableSet<E> set, E element);

}///:~