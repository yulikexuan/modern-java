//: com.yulikexuan.concurrency.taskexe.cancellation.TimedRun2.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import com.yulikexuan.concurrency.buildingblocks.synchronizers.LaunderThrowable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@AllArgsConstructor(staticName = "of")
public class TimedRun2 {

    private final ScheduledExecutorService scheduleExecutor;

    public void timedRun(final Runnable r, long timeout, TimeUnit unit)
            throws InterruptedException {

        class RethrowableTask implements Runnable {

            private volatile Throwable t;

            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    this.t = t;
                }
            }

            void rethrow() {
                if (t != null) {
                    throw LaunderThrowable.launderThrowable(t);
                }
            }
        }

        RethrowableTask task = new RethrowableTask();

        final Thread taskThread = new Thread(task);
        taskThread.start();

        scheduleExecutor.schedule(taskThread::interrupt, timeout, unit);

        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }

}///:~