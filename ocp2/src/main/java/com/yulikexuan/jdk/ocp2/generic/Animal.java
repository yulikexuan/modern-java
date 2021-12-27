//: com.yulikexuan.jdk.ocp2.generic.Animal.java

package com.yulikexuan.jdk.ocp2.generic;


import lombok.extern.slf4j.Slf4j;


@Slf4j
abstract class Animal {

    void checkup() {
        log.info(">>> {} checkup ... ", this.getClass().getSimpleName());
    }

}///:~