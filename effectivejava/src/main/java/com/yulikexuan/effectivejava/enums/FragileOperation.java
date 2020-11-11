//: com.yulikexuan.effectivejava.enums.FragileOperation.java


package com.yulikexuan.effectivejava.enums;


public enum FragileOperation {

    PLUS, MINUS, TIMES, DIVIDE;

    public double apply(double x, double y) {

        return switch (this) {
            case PLUS -> x + y;
            case MINUS -> x - y;
            case TIMES -> x * y;
            case DIVIDE -> x / y;
            default -> {
                throw new AssertionError(String.format("Undefined Operation %s",
                        this.toString()));
            }
        };

    }

}///:~
