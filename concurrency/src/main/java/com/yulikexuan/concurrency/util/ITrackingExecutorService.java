//: com.yulikexuan.concurrency.util.ITrackingExecutorService.java

package com.yulikexuan.concurrency.util;


import java.util.List;
import java.util.concurrent.ExecutorService;


public interface ITrackingExecutorService extends ExecutorService {

    List<Runnable> getCancelledTasks();

}///:~