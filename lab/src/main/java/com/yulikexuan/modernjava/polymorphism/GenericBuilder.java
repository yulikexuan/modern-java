//: com.yulikexuan.modernjava.polymorphism.GenericBuilder.java


package com.yulikexuan.modernjava.polymorphism;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GenericBuilder implements SelfAware<GenericBuilder> {

    @Override
    public GenericBuilder self() {
        logInfo();
        return this;
    }

    void logInfo() {
        log.info(">>>>>>> Return instance of {}", this.getClass().getSimpleName());
    }

}///:~