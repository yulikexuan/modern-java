//: com.yulikexuan.effectivejava.model.design.noinheritance.Sub.java


package com.yulikexuan.effectivejava.model.design.noinheritance;


import lombok.extern.slf4j.Slf4j;

import java.time.Instant;


@Slf4j
public class Sub extends Super {

    private Instant instant;

    Sub() {
        this.instant = Instant.now();
    }

    @Override
    void overrideMe() {
        log.info(">>>>>>> Working in Sub::overrideMe; The instant is {}",
                this.instant.toString());
    }

}///:~