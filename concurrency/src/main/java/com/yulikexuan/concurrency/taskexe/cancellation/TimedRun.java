//: com.yulikexuan.concurrency.taskexe.cancellation.TimedRun.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import com.yulikexuan.concurrency.buildingblocks.synchronizers.LaunderThrowable;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;


@Slf4j
@AllArgsConstructor(staticName = "of")
public class TimedRun {

    private final ExecutorService executor;

    public Future<?> timedRun(Runnable r, long timeout, TimeUnit unit)
            throws InterruptedException {

        Future<?> task = executor.submit(r);

        try {
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            // task will be cancelled below
            log.warn(">>>>>>> Timeout! Should cancel the task now.");
        } catch (ExecutionException e) {
            // exception thrown in task; rethrow
            log.error(">>>>>>> Caught error here, {}", e.getMessage());
            throw LaunderThrowable.launderThrowable(e.getCause());
        } finally {
            // Harmless if task already completed
            // interrupt if running
            log.warn(">>>>>>> Was the task cancelled? - {}",
                    task.cancel(true));
            return task;
        }

    }

}///:~