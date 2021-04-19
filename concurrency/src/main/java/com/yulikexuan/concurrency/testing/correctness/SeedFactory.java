//: com.yulikexuan.concurrency.testing.correctness.SeedFactory.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.NonNull;


final class SeedFactory {

    private SeedFactory() {}

    static SeedFactory newSeedFactory() {
        return new SeedFactory();
    }

    int initSeed(@NonNull final Object targetObj) {
        return (targetObj.hashCode() ^ (int) System.nanoTime());
    }

    int nextSeed(int seed) {
        return xorShift(seed);
    }

    private int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

}///:~