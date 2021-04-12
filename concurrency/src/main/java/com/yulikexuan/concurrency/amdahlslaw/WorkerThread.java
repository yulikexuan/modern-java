//: com.yulikexuan.concurrency.amdahlslaw.WorkerThread.java

package com.yulikexuan.concurrency.amdahlslaw;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;


/**
 * WorkerThread
 * <p/>
 * Serialized access to a task queue
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
@AllArgsConstructor(staticName = "of")
public class WorkerThread extends Thread {

    private final BlockingQueue<Runnable> queue;

    public void run() {
        while (true) {
            try {
                Runnable task = queue.take();
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; /* Allow thread to exit */
            }
        }
    }

}///:~