//: com.yulikexuan.kotlinlab.service.KotlinServiceProxyTest.java


package com.yulikexuan.kotlinlab.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class KotlinServiceProxyTest {

    private KotlinServiceProxy ksp;

    @BeforeEach
    void setUp() {
        this.ksp = new KotlinServiceProxy();
    }

    @Test
    void test_Kotlin_Greeting() {
        this.ksp.sayHelloToKotlin();
    }

}///:~