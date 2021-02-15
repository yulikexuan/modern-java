//: com.yulikexuan.concurrency.buildingblocks.cache.IComputable.java

package com.yulikexuan.concurrency.buildingblocks.cache;


@FunctionalInterface
public interface IComputable<A, V> {

    V compute(A arg) throws InterruptedException;

}///:~