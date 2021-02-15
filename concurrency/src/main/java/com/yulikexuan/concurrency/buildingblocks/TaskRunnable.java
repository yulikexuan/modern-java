//: com.yulikexuan.concurrency.buildingblocks.TaskRunnable.java

package com.yulikexuan.concurrency.buildingblocks;


import java.util.concurrent.BlockingQueue;


/*
 * Restore the interrupt
 * Sometimes you cannot throw InterruptedException, for instance when your code
 * is part of a Runnable
 * In these situations, you must catch InterruptedException and restore the
 * interrupted status by calling interrupt on the current thread, so that code
 * higher up the call stack can see that an interrupt was issued
 *
 */
public class TaskRunnable implements Runnable {

    BlockingQueue<Task> queue;

    @Override
    public void run() {
        try {
            processTask(queue.take());
        } catch (InterruptedException e) {
            // restore interrupted status
            Thread.currentThread().interrupt();
        }
    }

    void processTask(Task task) {
        // Handle the task
    }

    interface Task {
    }

}///:~