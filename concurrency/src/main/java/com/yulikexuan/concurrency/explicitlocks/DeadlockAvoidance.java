//: com.yulikexuan.concurrency.explicitlocks.DeadlockAvoidance.java

package com.yulikexuan.concurrency.explicitlocks;


import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


class DeadlockAvoidance {

    private static ThreadLocalRandom rnd = ThreadLocalRandom.current();

    private static final int DELAY_FIXED = 1;
    private static final int DELAY_RANDOM = 2;

    boolean transferMoney(Account fromAcct, Account toAcct,
                          DollarAmount amount, long timeout, TimeUnit unit)
            throws InsufficientFundsException, InterruptedException {

        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        while (true) {

            if (fromAcct.lock.tryLock()) {
                try {
                    if (toAcct.lock.tryLock()) {
                        try {
                            if (fromAcct.getBalance().compareTo(amount) < 0)
                                throw new InsufficientFundsException();
                            else {
                                fromAcct.debit(amount);
                                toAcct.credit(amount);
                                return true;
                            }
                        } finally {
                            toAcct.lock.unlock();
                        }
                    }
                } finally {
                    fromAcct.lock.unlock();
                }
            }

            if (System.nanoTime() >= stopTime) {
                return false;
            }

            TimeUnit.NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
        }
    }

    static long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return DELAY_FIXED;
    }

    static long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return DELAY_RANDOM;
    }

}

class Account {

    public Lock lock;

    void debit(DollarAmount d) {
    }

    void credit(DollarAmount d) {
    }

    DollarAmount getBalance() {
        return null;
    }
}

class InsufficientFundsException extends Exception {
}

class DollarAmount implements Comparable<DollarAmount> {

    public int compareTo(DollarAmount other) {
        return 0;
    }

    DollarAmount(int dollars) {
    }

}

///:~