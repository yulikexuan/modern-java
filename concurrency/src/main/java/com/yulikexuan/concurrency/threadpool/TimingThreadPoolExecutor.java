//: com.yulikexuan.concurrency.threadpool.TimingThreadPoolExecutor.java

package com.yulikexuan.concurrency.threadpool;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class TimingThreadPoolExecutor extends ThreadPoolExecutor {

    static final String THREAD_POOL_NAME = "Timing_Thread_Pool";

    static final int CORE_POOL_SIZE = 1;
    static final int MAX_POOL_SIZE = 1;
    static final long KEEP_ALIVE_TIME = 0L;

    private TimingThreadPoolExecutor() {
        super(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, null,
                NamedThreadFactory.of(THREAD_POOL_NAME));
    }

    public static TimingThreadPoolExecutor defaultTimingThreadPoolExecutor() {
        return new TimingThreadPoolExecutor();
    }

    private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        log.info(">>>>>>> Thread %s: start {}", t.getName(), r);
        this.startTime.set(System.nanoTime());
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {

        super.afterExecute(r, t);

        if (t == null && r instanceof Future<?> && ((Future<?>) r).isDone()) {
            try {
                Object result = ((Future<?>) r).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                // ignore/reset
                Thread.currentThread().interrupt();
            }
        }

        long taskTime = System.nanoTime() - this.startTime.get();

        this.numTasks.incrementAndGet();
        this.totalTime.addAndGet(taskTime);

        log.info(">>>>>>> Thread {}: end {}, time = {} ns", t, r, taskTime);
    }

    @Override
    protected void terminated() {
        try {
            log.info(">>>>>>> Terminated: avg time = {} ns",
                    this.totalTime.get() / this.numTasks.get());
        } finally {
            super.terminated();
        }
    }

}///:~