//: com.yulikexuan.effectivejava.generics.Union.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.Sets;

import java.util.Set;


/*
 * Generic union method with wildcard types for enhanced flexibility
 * (Pages 142-3)
 */
public class Union {

    public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {

        Set<E> result = Sets.newHashSet(s1);

        result.addAll(s2);

        return result;
    }

}///:~