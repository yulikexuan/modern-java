//: com.yulikexuan.concurrency.thread.sharing.NoVisibility.java

package com.yulikexuan.concurrency.thread.sharing;


import javax.annotation.concurrent.NotThreadSafe;
import java.util.concurrent.TimeUnit;


@NotThreadSafe
public class NoVisibility {

    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {

        public void run() {
            int i = 0;
            while (!ready) {
                ++i; // Thread.yield();
            }
            System.out.println(">>>>>> The number is " + number);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReaderThread().start();
        TimeUnit.SECONDS.sleep(1);
        number = 42;
        ready = true;
    }

}///:~