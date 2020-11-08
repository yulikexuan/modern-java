//: com.yulikexuan.modernjava.inheritance.rules.Bar.java


package com.yulikexuan.modernjava.inheritance.rules;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;


@Slf4j
class Bar extends Foo {

    private final ThreadLocalRandom random;

    Bar() {
        this.random = ThreadLocalRandom.current();
    }

    @Override
    Integer generate() {
        return this.random.nextInt(0, 100);
    }

}///:~