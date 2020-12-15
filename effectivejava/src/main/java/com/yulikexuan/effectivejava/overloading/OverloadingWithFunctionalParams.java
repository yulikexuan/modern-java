//: com.yulikexuan.effectivejava.overloading.OverloadingWithFunctionalParams.java

package com.yulikexuan.effectivejava.overloading;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;


@Slf4j
class OverloadingWithFunctionalParams {

    static void runFunctionally(Runnable runnable) {
        runnable.run();
    }

    static <T> void runFunctionally(Callable<T> callable) {
        try {
            callable.call();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}///:~