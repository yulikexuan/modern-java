//: com.yulikexuan.concurrency.util.ICancellableTask.java

package com.yulikexuan.concurrency.util;


import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;


public interface ICancellableTask<T> extends Callable<T> {

    void cancel();
    boolean isCancelled();
    RunnableFuture<T> newTask();

}///:~