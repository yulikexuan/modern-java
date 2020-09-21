//: com.yulikexuan.modernjava.algorithms.NumberBinarySearchTest.java


package com.yulikexuan.modernjava.algorithms;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;


public class NumberBinarySearchTest {

    private int[] numbers;
    private ThreadLocalRandom random;

    @BeforeEach
    void setUp() {
        this.random = ThreadLocalRandom.current();
        this.numbers = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    @Test
    @RepeatedTest(10)
    void test_Iterative_Binary_Search() {

        // Given
        int value = this.random.nextInt(0, 10);

        // When
        int index = NumberBinarySearch.iterativeBinarySearch(this.numbers,
                0, this.numbers.length - 1, value);

        // Then
        assertThat(index).isEqualTo(value);
    }

}///:~