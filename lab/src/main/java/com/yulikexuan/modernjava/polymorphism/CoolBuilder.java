//: com.yulikexuan.modernjava.polymorphism.CoolBuilder.java


package com.yulikexuan.modernjava.polymorphism;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CoolBuilder extends GenericBuilder  {

    @Override
    public CoolBuilder self() {
        logInfo();
        return this;
    }


}///:~