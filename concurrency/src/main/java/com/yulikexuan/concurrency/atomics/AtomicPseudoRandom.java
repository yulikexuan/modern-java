//: com.yulikexuan.concurrency.atomics.AtomicPseudoRandom.java

package com.yulikexuan.concurrency.atomics;


import com.yulikexuan.concurrency.util.PseudoRandom;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * AtomicPseudoRandom
 * <p/>
 * Random number generator using AtomicInteger
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class AtomicPseudoRandom extends PseudoRandom {

    private AtomicInteger seed;

    private AtomicPseudoRandom(int seed) {
        this.seed = new AtomicInteger(seed);
    }

    public static AtomicPseudoRandom of(int seed) {
        return new AtomicPseudoRandom(seed);
    }

    public int nextInt(int n) {

        while (true) {

            int s = this.seed.get();
            int nextSeed = calculateNext(s);

            if (this.seed.compareAndSet(s, nextSeed)) {
                int remainder = s % n;
                return remainder > 0 ? remainder : remainder + n;
            }
        }

    }

}///:~