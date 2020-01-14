//: com.yulikexuan.modernjava.fp.RecursiveFactorialCalaulator.java


package com.yulikexuan.modernjava.fp;


public class RecursiveFactorialCalaulator implements IFactorialCalaulator {

    @Override
    public long calculateFactorial(long n) {

        if (n <= 0) {
            throw new IllegalArgumentException("The argument should not be " +
                    "negative or zero.");
        }

        return (n == 1) ? 1 : n * calculateFactorial(n - 1);
    }

}///:~