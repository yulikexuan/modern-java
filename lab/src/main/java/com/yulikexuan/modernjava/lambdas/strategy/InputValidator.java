//: com.yulikexuan.modernjava.lambdas.strategy.InputValidator.java


package com.yulikexuan.modernjava.lambdas.strategy;


import lombok.AllArgsConstructor;

import java.util.function.Predicate;


@AllArgsConstructor(staticName = "of")
public class InputValidator {

    private final Predicate<String> validationStrtegy;

    public boolean validate(String input) {
        return this.validationStrtegy.test(input);
    }

}///:~