//: com.yulikexuan.effectivejava.concurrency.ThreadTerminator.java

package com.yulikexuan.effectivejava.concurrency;


import java.util.concurrent.TimeUnit;


public class ThreadTerminator {

    private static boolean stopRequested;

    public static void main(String... args) throws InterruptedException {
        // createAndStopAThreadFailed();
        createAndStopAThreadSuccessfully();
    }

    /*
     * Broken! - How long would you expect this program to run?  (Page 312)
     */
    private static void createAndStopAThreadFailed() throws InterruptedException {

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

    static synchronized void requestStop() {
        stopRequested = true;
    }

    static synchronized boolean isStopRequested() {
        return stopRequested;
    }

    private static void createAndStopAThreadSuccessfully() throws InterruptedException {

        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!isStopRequested()) {
                i++;
            }
        });

        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);

        requestStop();
    }

}///:~