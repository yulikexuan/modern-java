//: com.yulikexuan.effectivejava.annotation.RepeatableExceptionAnnotationSample.java

package com.yulikexuan.effectivejava.annotation;


import com.google.common.collect.Lists;

import java.util.List;


public class RepeatableExceptionAnnotationSample {

    // Passed
    @AnExceptionTest(NullPointerException.class)
    @AnExceptionTest(IndexOutOfBoundsException.class)
    static void m0() {
        List<String> list = Lists.newArrayList();
        list.addAll(5, null);
    }

    // Failed, throw IndexOutOfBoundsException actually
    @AnExceptionTest(NullPointerException.class)
    static void m1() {
        List<String> list = Lists.newArrayList();
        list.addAll(5, null);
    }

}///:~