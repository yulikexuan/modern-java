//: com.yulikexuan.modernjava.fp.StreamFactorialCalaulator.java


package com.yulikexuan.modernjava.fp;


import java.util.stream.LongStream;


public class StreamFactorialCalaulator implements IFactorialCalaulator {

    @Override
    public long calculateFactorial(long n) {

        if (n <= 0) {
            throw new IllegalArgumentException("The argument should not be " +
                    "negative or zero.");
        }

        return LongStream.rangeClosed(1, n)
                .reduce(1, (a, b) -> a * b);
    }

}///:~