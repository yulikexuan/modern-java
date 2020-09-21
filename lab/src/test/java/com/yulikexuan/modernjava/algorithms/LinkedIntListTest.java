//: com.yulikexuan.modernjava.algorithms.LinkedIntListTest.java


package com.yulikexuan.modernjava.algorithms;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class LinkedIntListTest {

    private static final int SIZE = 8;

    private LinkedIntList linkedIntList;

    @BeforeEach
    void setUp() {
        this.linkedIntList = LinkedIntList.of();
    }

    @Test
    void test_Reversing() {

        // Given & When
        this.linkedIntList.reverse();

        // Then
        this.linkedIntList.print();
    }

}///:~