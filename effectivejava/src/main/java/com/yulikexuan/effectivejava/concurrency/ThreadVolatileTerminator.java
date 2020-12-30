//: com.yulikexuan.effectivejava.concurrency.ThreadVolatileTerminator.java

package com.yulikexuan.effectivejava.concurrency;


import java.util.concurrent.TimeUnit;

public class ThreadVolatileTerminator {

    private static volatile boolean stopRequested;

    public static void main(String... args) throws InterruptedException {
        createAndStopAThread();
    }

    private static void createAndStopAThread() throws InterruptedException {

        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested) {
                i++;
            }
        });

        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);

        stopRequested = true;
    }

}///:~