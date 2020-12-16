//: com.yulikexuan.effectivejava.varargs.NonSingleVarargs.java

package com.yulikexuan.effectivejava.varargs;


import java.util.Arrays;


class NonSingleVarargs {

    static int findMin(int... numbers) {
        return Arrays.stream(numbers).min().orElseThrow(
                IllegalArgumentException::new);
    }

    static int min(int firstInt, int... numbers) {
        int min = firstInt;
        for (int i : numbers) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    static int min2(final int firstInt, final int... numbers) {
        return Integer.min(firstInt, Arrays.stream(numbers).min().orElse(firstInt));
    }

}///:~