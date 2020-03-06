//: com.yulikexuan.modernjava.concurrency.executors.DaemonThreadDemo.java


package com.yulikexuan.modernjava.concurrency.executors;


import com.google.common.util.concurrent.MoreExecutors;
import lombok.Getter;

import java.util.concurrent.*;

/*
 * Java offers two types of thresds: User threads and daemon threads
 *
 * User Threads:
 *     - High-priority
 *     - The JVM will wait for any user thread to complete its task before
 *       terminating it
 *
 * Daemon Threads:
 *     - Low-priority
 *     - For providing services to user threads
 *
 * Since daemon threads are meant to serve user threads and are only needed
 * while user threads are running, they won't prevent the JVM from exiting once
 * all user threads have finished their execution
 *
 * Daemon threads are not recommended for I/O tasks
 *
 * Calling Thread.join() on a running daemon thread can block the shutdown of
 * the application
 */
@Getter
public class DaemonThreadDemo {

    public static final int NUMBER_OF_THREADS = 4;

    private final ScheduledExecutorService exitingExecutorService;

    private DaemonThreadDemo(ScheduledExecutorService exitingExecutorService) {
        this.exitingExecutorService = exitingExecutorService;
    }

    public static DaemonThreadDemo of(long terminationTimeout,
                                      TimeUnit timeUnit) {

        ScheduledThreadPoolExecutor scheduledExecutor =
                (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(
                        NUMBER_OF_THREADS);

        ScheduledExecutorService exitingExecutorService =
                MoreExecutors.getExitingScheduledExecutorService(
                        scheduledExecutor, terminationTimeout, timeUnit);

        return new DaemonThreadDemo(exitingExecutorService);
    }

}///:~