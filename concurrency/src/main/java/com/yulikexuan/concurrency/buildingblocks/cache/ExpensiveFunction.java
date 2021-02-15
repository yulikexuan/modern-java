//: com.yulikexuan.concurrency.buildingblocks.cache.ExpensiveFunction.java

package com.yulikexuan.concurrency.buildingblocks.cache;


import lombok.NonNull;

import java.math.BigInteger;


public class ExpensiveFunction implements IComputable<String, BigInteger> {

    @Override
    public BigInteger compute(@NonNull String arg) throws InterruptedException {
        Thread.sleep(1000);
        return new BigInteger(arg);
    }

}///:~