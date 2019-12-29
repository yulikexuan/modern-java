//: com.yulikexuan.modernjava.fp.IterativeFactorialCalaulator.java


package com.yulikexuan.modernjava.fp;


public class IterativeFactorialCalaulator implements IFactorialCalaulator {

    @Override
    public long calculateFactorial(long n) {

        if (n <= 0) {
            throw new IllegalArgumentException("The argument should not be " +
                    "negative or zero.");
        }

        long r = 1;

        for (long i = 1; i <= n; i++) {
            r *= i;
        }

        return r;
    }

}///:~