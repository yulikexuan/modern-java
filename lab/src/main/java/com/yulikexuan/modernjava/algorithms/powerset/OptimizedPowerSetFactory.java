//: com.yulikexuan.modernjava.algorithms.powerset.OptimizedPowerSetFactory.java

package com.yulikexuan.modernjava.algorithms.powerset;


import com.google.common.collect.Lists;

import java.util.List;


public final class OptimizedPowerSetFactory {

    public static final List<List<Boolean>> newBinaryPowerSet(int n) {
        List<List<Boolean>> binPowerList = Lists.newArrayList();
        int powerSetSize = 1 << n;
        for (int i = 0; i < powerSetSize; i++) {
            List<Boolean> subList = Lists.newArrayListWithCapacity(n);
            for (int j = 0; j < n; j++) {
                subList.add(((1 << j) & i) > 0);
            }
            binPowerList.add(subList);
        }
        return binPowerList;
    }

    public static int getGrayCodeEquivalent(int n) {
        return n ^ (n >> 1);
    }

}///:~