//: com.yulikexuan.concurrency.util.PseudoRandom.java

package com.yulikexuan.concurrency.util;

public class PseudoRandom {

    public int calculateNext(int prev) {
        prev ^= prev << 6;
        prev ^= prev >>> 21;
        prev ^= (prev << 7);
        return prev;
    }

}///:~