//: com.yulikexuan.effectivejava.generics.varargs.PickTwo.java


package com.yulikexuan.effectivejava.generics.varargs;


import java.util.concurrent.ThreadLocalRandom;


public class PickTwo {

    // UNSAFE - Exposes a reference to its generic parameter array!
    static <T> T[] toArray(T... args) {
        return args;
    }

    static <T> T[] pickTwo(T a, T b, T c) {

        switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0: return toArray(a, b);
            case 1: return toArray(a, c);
            case 2: return toArray(b, c);
        }

        throw new AssertionError(); // Can't get here
    }

}///:~