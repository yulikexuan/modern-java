//: com.yulikexuan.concurrency.threadpool.NamedThread.java

package com.yulikexuan.concurrency.threadpool;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class NamedThread extends Thread {

    public static final String DEFAULT_NAME = "JavaGuruThread";

    private static volatile boolean debugLifecycle = false;

    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();

    private NamedThread(Runnable runnable, String name,
                        UncaughtExceptionHandler uncaughtExceptionHandler) {

        super(runnable, name + "-" + created.incrementAndGet());

        this.setUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    public static NamedThread of(@NonNull Runnable runnable) {
        return NamedThread.of(runnable, DEFAULT_NAME);
    }

    public static NamedThread of(@NonNull Runnable runnable,
                                 @NonNull String threadName) {

        return NamedThread.of(runnable, threadName,
                NamedThread::logUncaughtException);
    }

    public static NamedThread of(
            @NonNull Runnable runnable, @NonNull String threadName,
            @NonNull UncaughtExceptionHandler exceptionHandler) {

        return new NamedThread(runnable, threadName, exceptionHandler);
    }

    @Override
    public void run() {

        // Copy debug flag to ensure consistent value throughout.
        boolean debug = debugLifecycle;

        if (debug) {
            log.info(">>>>>>> Created " + this.getName());
        }

        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) {
                log.info(">>>>>>> Exiting {}", this.getName());
            }
        }
    }

    public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }

    public static boolean isDebug() {
        return debugLifecycle;
    }

    public static void setDebug(boolean isDebug) {
        debugLifecycle = isDebug;
    }

    private static void logUncaughtException(Thread thread,
                                             Throwable throwable) {

        log.error(">>>>>>> UNCAUGHT in thread {} : {}",
                thread.getName(), throwable.getMessage());
    }

}///:~