//: com.yulikexuan.concurrency.taskexe.cancellation.NoncancelableTask.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

/**
 * NoncancelableTask
 * <p/>
 * Noncancelable task that restores interruption before exit
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
@AllArgsConstructor(staticName = "of")
public class NoncancelableTask {

    private final BlockingQueue<Task> taskQueue;

    public Task getNextTask() {

        boolean interrupted = false;

        try {
            while (true) {
                try {
                    log.info(">>>>>>> Try get ... ... ");
                    return taskQueue.take();
                } catch (InterruptedException e) {
                    log.warn(">>>>>>> Interrupted!!! ");
                    interrupted = true;
                    // fall through and retry
                }
            }
        } finally {
            /*
             * Note: If the JVM exits while the try or catch code is being
             * executed, then the finally block may not execute. Likewise, if
             * the thread executing the try or catch code is interrupted or
             * killed, the finally block may not execute even though the
             * application as a whole continues.
             */
            if (interrupted) {
                Thread.currentThread().interrupt();
                log.info(">>>>>>> Interrupted finally !!! ");
            }
            log.info(">>>>>>> Finished !!! ");
        }
    }

}

interface Task {
}

///:~