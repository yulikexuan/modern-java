//: com.yulikexuan.concurrency.buildingblocks.synchronizers.Preloader.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * Preloader
 *
 * Using FutureTask to preload data that is needed later
 *
 * @author Brian Goetz and Tim Peierls
 */

public class Preloader {

    ProductInfo loadProductInfo() throws DataLoadException {
        return null;
    }

    private final FutureTask<ProductInfo> future =
            new FutureTask<>(new Callable<ProductInfo>() {
                public ProductInfo call() throws DataLoadException {
                    return loadProductInfo();
                }
            });

    private final Thread thread = new Thread(future);

    /*
     * Provides a start method to start the thread, since it is inadvisable to
     * start a thread from a constructor or static initializer
     */
    public void start() {
        thread.start();
    }

    public ProductInfo get() throws DataLoadException, InterruptedException {

        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException) {
                throw (DataLoadException) cause;
            } else {
                throw LaunderThrowable.launderThrowable(cause);
            }
        }
    }

    interface ProductInfo {
    }

}

class DataLoadException extends Exception { }

///:~