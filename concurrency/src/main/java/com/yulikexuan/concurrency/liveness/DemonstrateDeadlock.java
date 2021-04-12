//: com.yulikexuan.concurrency.liveness.DemonstrateDeadlock.java

package com.yulikexuan.concurrency.liveness;


import java.util.concurrent.ThreadLocalRandom;

import static com.yulikexuan.concurrency.liveness.DynamicOrderDeadlock.*;


public class DemonstrateDeadlock {

    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1_000_000;

    public static void main(String[] args) {

        final ThreadLocalRandom rnd = ThreadLocalRandom.current();

        final Account[] accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account();
        }

        class TransferThread extends Thread {
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
                    DollarAmount amount = new DollarAmount(
                            rnd.nextInt(1000));
                    try {
                        DynamicOrderDeadlock.transferMoney(
                                accounts[fromAcct], accounts[toAcct], amount);
                    } catch (DynamicOrderDeadlock.InsufficientFundsException
                            ignored) {
                        // Log info
                    }
                }
            }
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }

    }

}///:~