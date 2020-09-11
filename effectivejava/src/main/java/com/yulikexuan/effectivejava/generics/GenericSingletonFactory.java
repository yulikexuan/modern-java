//: com.yulikexuan.effectivejava.generics.GenericSingletonFactory.java


package com.yulikexuan.effectivejava.generics;


import java.util.function.UnaryOperator;


class GenericSingletonFactory {

    // Generic singlton factory pattern
    private static final UnaryOperator<Object> IDENTITY_FUNC = (t) -> t;

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FUNC;
    }

}///:~