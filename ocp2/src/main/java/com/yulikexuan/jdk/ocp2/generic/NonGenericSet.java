//: com.yulikexuan.jdk.ocp2.generic.NonGenericSet.java

package com.yulikexuan.jdk.ocp2.generic;


import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


@Slf4j
final class NonGenericSet {

    void before() {
        Set set = new TreeSet();
        set.add("2");
        set.add(3);
        set.add("1");
        Iterator it = set.iterator();
        while (it.hasNext()) log.info(it.next() + " ");
    }

}///:~