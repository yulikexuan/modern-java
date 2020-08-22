//: com.yulikexuan.effectivejava.model.design.noinheritance.Super.java


package com.yulikexuan.effectivejava.model.design.noinheritance;


import lombok.extern.slf4j.Slf4j;


@Slf4j
class Super {

    Super() {
        this.overrideMe();
    }

    void overrideMe() {
        log.info(">>>>>>> Working in Super::overrideMe");
    }

}///:~