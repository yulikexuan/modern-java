//: com.yulikexuan.concurrency.util.CancellingExecutor.java

package com.yulikexuan.concurrency.util;


import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.*;


@ThreadSafe
public class CancellingExecutor extends ThreadPoolExecutor  {

    public CancellingExecutor(int corePoolSize, int maximumPoolSize,
                              long keepAliveTime, TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize,
                              long keepAliveTime, TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize,
                              long keepAliveTime, TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              RejectedExecutionHandler handler) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                handler);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize,
                              long keepAliveTime, TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, handler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof ICancellableTask) {
            return ((ICancellableTask<T>) callable).newTask();
        } else {
            return super.newTaskFor(callable);
        }
    }

}///:~