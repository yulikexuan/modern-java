//: com.yulikexuan.modernjava.fp.TailRecursiveFactorialCalaulator.java


package com.yulikexuan.modernjava.fp;


public class TailRecursiveFactorialCalaulator implements IFactorialCalaulator {

    @Override
    public long calculateFactorial(long n) {
        return this.tailRecursive(1, n);
    }

    private long tailRecursive(long acc, long n) {
        return (n == 1) ? acc : tailRecursive(acc * n, n - 1);
    }

}///:~