//: com.yulikexuan.concurrency.atomics.ReentrantLockPseudoRandom.java

package com.yulikexuan.concurrency.atomics;


import com.yulikexuan.concurrency.util.PseudoRandom;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * ReentrantLockPseudoRandom
 * <p/>
 * Random number generator using ReentrantLock
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ReentrantLockPseudoRandom extends PseudoRandom {

    private final Lock lock = new ReentrantLock(false);

    private int seed;

    private ReentrantLockPseudoRandom(int seed) {
        this.seed = seed;
    }

    static ReentrantLockPseudoRandom of(int seed) {
        return new ReentrantLockPseudoRandom(seed);
    }

    public int nextInt(int n) {

        this.lock.lock();

        try {

            int s = this.seed;
            this.seed = calculateNext(s);
            int remainder = s % n;

            return remainder > 0 ? remainder : remainder + n;

        } finally {
            this.lock.unlock();
        }

    }

}///:~