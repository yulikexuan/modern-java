//: com.yulikexuan.modernjava.console.ProgressBar.java


package com.yulikexuan.modernjava.console;


import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ProgressBar {

    public static void main(String[] args) {

        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        ExecutorService executorService = MoreExecutors.getExitingExecutorService(
                executor, 100, TimeUnit.MILLISECONDS);

        System.out.print("Mission started: \n");

        executorService.submit(() -> {
            while (true) {
                System.out.print(' ');
                for (int j = 0; j < 9; j++) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print('>');
                }
                System.out.print("\r");
            }
        });

        for (int j = 0; j < 10; j++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("\n\nMission completed!");
    }

}///:~