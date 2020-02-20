//: com.yulikexuan.modernjava.generics.inference.TypeInferenceTest.java


package com.yulikexuan.modernjava.generics.inference;


import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;


public class TypeInferenceTest {

    private void printNames(List<String> names) {
        names.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    @Test
    void test_Empty_List_Method_Of_Collections_Class() {

        // Given

        // Pre Java 8
        printNames(Collections.<String>emptyList());

        // As of Java 8
        printNames(Collections.emptyList());
    }

}///:~