//: com.yulikexuan.concurrency.testing.correctness.BigData.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.NoArgsConstructor;


/*
 * This class is used for testing resource management
 */
final class BigData {

    double[] data = new double[100_000];

    private BigData() {}

    static BigData of() {
        return new BigData();
    }

}///:~