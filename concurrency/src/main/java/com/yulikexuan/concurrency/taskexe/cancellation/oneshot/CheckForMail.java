//: com.yulikexuan.concurrency.taskexe.cancellation.oneshot.CheckForMail.java

package com.yulikexuan.concurrency.taskexe.cancellation.oneshot;


import lombok.NonNull;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CheckForMail
 * <p/>
 * Using a private \Executor whose lifetime is bounded by a method call
 *
 * @author Brian Goetz and Tim Peierls
 */
public class CheckForMail {

    public boolean checkMail(@NonNull Set<String> hosts,
                             long timeout, TimeUnit unit)
            throws InterruptedException {

        ExecutorService exec = Executors.newCachedThreadPool();

        /*
         * The reason an AtomicBoolean is used instead of a volatile boolean is
         * that in order to access the hasNewMail flag from the inner Runnable,
         * it would have to be final, which would preclude modifying it
         */
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);

        try {
            for (final String host : hosts) {
                exec.execute(() -> {
                    if (checkMail(host)) {
                        hasNewMail.set(true);
                    }
                });
            }
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout, unit);
        }

        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        // Check for mail
        return false;
    }

}///:~