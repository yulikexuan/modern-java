//: com.yulikexuan.concurrency.errorhandling.UncaughtExceptionLoggingHandler.java

package com.yulikexuan.concurrency.errorhandling;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UncaughtExceptionLoggingHandler implements
        Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error(">>>>>>> Thread {} terminated with exception: {}", 
                t.getName(), e);
    }

}///:~