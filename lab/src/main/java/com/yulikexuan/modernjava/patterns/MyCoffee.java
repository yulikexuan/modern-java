//: com.yulikexuan.modernjava.patterns.MyCoffee.java


package com.yulikexuan.modernjava.patterns;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class MyCoffee extends CustomCoffee {

    private final RegularCoffee regularCoffee;

    @Override
    double getPrice() {
        return this.regularCoffee.getPrice() * 1.1;
    }

}///:~