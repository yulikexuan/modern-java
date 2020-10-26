//: com.yulikexuan.effectivejava.generics.varargs.Dangerous.java


package com.yulikexuan.effectivejava.generics.varargs;


import java.util.List;


public class Dangerous {

    // @SafeVarargs
    // Mixing generics and varargs can violate type safety!
    static void dangerous(List<String>... stringLists) {

        List<Integer> intList = List.of(42);

        Object[] objects = stringLists;
        objects[0] = intList; // Heap pollution

        String s = stringLists[0].get(0); // ClassCastException
    }

}///:~