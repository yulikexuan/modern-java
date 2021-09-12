//: com.yulikexuan.jdk.ocp2.enums.TestEnum.java

package com.yulikexuan.jdk.ocp2.enums;


import lombok.extern.slf4j.Slf4j;


@Slf4j
class TestEnum {

    static final String KEY = "1234567";

    static Animals a;

    public static void main(String[] args) {
        log.info(">>>>>>> Static field a is {}", a);
        log.info(">>>>>>> {} {}", a.DOG.sound, a.FISH.sound);
        TestEnum te = null;
        log.info(">>>>>>> The key is {}", te.KEY);
    }

}///:~