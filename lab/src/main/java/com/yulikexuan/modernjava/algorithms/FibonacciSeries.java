//: com.yulikexuan.modernjava.algorithms.FibonacciSeries.java


package com.yulikexuan.modernjava.algorithms;


class FibonacciSeries {

    static long recursiveCalaulate(int n) {
        if ((n == 0) || (n == 1)) {
            return n;
        }
        return recursiveCalaulate(n - 2) + recursiveCalaulate(n - 1);
    }

}///:~