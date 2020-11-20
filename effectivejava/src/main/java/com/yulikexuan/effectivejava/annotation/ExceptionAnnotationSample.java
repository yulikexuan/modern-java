//: com.yulikexuan.effectivejava.annotation.ExceptionAnnotationSample.java

package com.yulikexuan.effectivejava.annotation;


import com.google.common.collect.Lists;

import java.util.List;


class ExceptionAnnotationSample {

    @AnyExceptionTest(ArithmeticException.class)
    static void m1() {  // Test should pass
        int i = 0;
        i = i / i;
    }

    @AnyExceptionTest(ArithmeticException.class)
    static void m2() {  // Should fail (wrong exception)
        int[] a = new int[0];
        int i = a[1];
    }

    @AnyExceptionTest(ArithmeticException.class)
    static void m3() { }  // Should fail (no exception)

    @AnyExceptionTest(NullPointerException.class)
    void m4() {} // Invalid

    @AnyExceptionTest({
            IndexOutOfBoundsException.class,
            NullPointerException.class})
    static void m5() {
        List<String> list = Lists.newArrayList();
        list.addAll(5, null);
    }

}///:~