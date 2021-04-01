//: com.yulikexuan.concurrency.jvmshutdown.JvmShutdownHook.java

package com.yulikexuan.concurrency.jvmshutdown;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;


@Slf4j
@AllArgsConstructor(staticName = "of")
public class JvmShutdownHook implements Runnable {

    private final Callable<?> hook;

    @Override
    public void run() {
        try {
            hook.call();
        } catch (Exception e) {
            log.error(">>>>>>> Caught Exception {} when shutdown JVM",
                    e.getClass().getSimpleName());
        } finally {
            log.info(">>>>>>> The shutdown hook was completed.");
        }
    }

}///:~