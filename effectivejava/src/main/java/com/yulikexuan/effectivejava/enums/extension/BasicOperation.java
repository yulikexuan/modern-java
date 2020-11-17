//: com.yulikexuan.effectivejava.enums.extension.BasicOperation.java

package com.yulikexuan.effectivejava.enums.extension;


import java.util.function.DoubleBinaryOperator;


public enum BasicOperation implements DoubleBinaryOperator {

    PLUS("+") {
        public double applyAsDouble(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        public double applyAsDouble(double x, double y) {
            return x - y;
        }
    },
    TIMES("*") {
        public double applyAsDouble(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        public double applyAsDouble(double x, double y) {
            return x / y;
        }
    };

    private final String symbol;

    BasicOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

}///:~
