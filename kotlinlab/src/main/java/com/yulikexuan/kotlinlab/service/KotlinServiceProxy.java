//: com.yulikexuan.kotlinlab.service.KotlinServiceProxy.java


package com.yulikexuan.kotlinlab.service;


class KotlinServiceProxy {

    void sayHelloToKotlin() {
        new HelloKotlinService().greeting();
    }

}///:~