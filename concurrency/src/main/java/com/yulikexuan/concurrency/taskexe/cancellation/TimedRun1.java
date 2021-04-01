//: com.yulikexuan.concurrency.taskexe.cancellation.TimedRun1.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import lombok.AllArgsConstructor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * InterruptBorrowedThread
 * <p/>
 * Scheduling an interrupt on a borrowed thread
 *
 * @author Brian Goetz and Tim Peierls
 */
@AllArgsConstructor(staticName = "of")
public class TimedRun1 {

    private final ScheduledExecutorService scheduledExecutor;

    /*
     * Don’t do this!
     *
     * This method violates the rules: you should know a thread’s interruption
     * policy before interrupting it
     *
     * Since timedRun can be called from an arbitrary thread, it cannot know
     * the calling thread’s interruption policy
     *
     * If the task completes before the timeout, the cancellation task that
     * interrupts the thread in which timedRun was called could go off after
     * timedRun has returned to its caller
     *
     * We don’t know what code will be running when that happens, but the result
     * won’t be good
     *
     * Further, if the task is not responsive to interruption, timedRun will not
     * return until the task finishes, which may be long after the desired
     * timeout (or even not at all)
     *
     * A timed run service that doesn’t return after the specified time is
     * likely to be irritating to its callers
     */
    public void timedRun(Runnable r, long timeout, TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();
        this.scheduledExecutor.schedule(taskThread::interrupt, timeout, unit);
        r.run();
    }

}///:~