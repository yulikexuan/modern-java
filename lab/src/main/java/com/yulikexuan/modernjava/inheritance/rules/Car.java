//: com.yulikexuan.modernjava.inheritance.rules.Car.java


package com.yulikexuan.modernjava.inheritance.rules;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@RequiredArgsConstructor
abstract class Car {

    final protected int limit;

    // invariant: speed < limit;
    protected int speed;

    // postcondition: speed < limit
    protected abstract void accelerate();

}///:~