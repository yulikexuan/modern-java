//: com.yulikexuan.effectivejava.annotation.AnnotationSample.java

package com.yulikexuan.effectivejava.annotation;


class AnnotationSample {

    // Should pass
    @EffectiveTest
    static void m1() {}

    static void m2() {}

    // Should fail
    @EffectiveTest
    static void m3() {
        throw new RuntimeException("m3");
    }

    static void m4() {}

    // Invalid use, should be static mehtod
    @EffectiveTest
    void m5() {}

    static void m6() {}

    // Should fail
    @EffectiveTest
    static void m7() {
        throw new RuntimeException("m7");
    }

    static void m8() {}

}///:~