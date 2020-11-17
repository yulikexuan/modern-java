//: com.yulikexuan.effectivejava.enums.extension.ScienceOperation.java

package com.yulikexuan.effectivejava.enums.extension;


import java.util.function.DoubleBinaryOperator;


public enum ScienceOperation implements DoubleBinaryOperator {

    EXP("^") {
        public double applyAsDouble(double x, double y) {
            return Math.pow(x, y);
        }
    },
    REMAINDER("%") {
        @Override
        public double applyAsDouble(double x, double y) {
            return x % y;
        }
    };

    private final String symbol;

    ScienceOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override public String toString() {
        return symbol;
    }

}///:~
