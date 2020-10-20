//: com.yulikexuan.modernjava.algorithms.FibonacciSeriesTest.java


package com.yulikexuan.modernjava.algorithms;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class FibonacciSeriesTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Calculate_Fibonacci_Number_Recursively() {

        // Given
        int index = 7;

        // When
        long fibonacci7th = FibonacciSeries.recursiveCalaulate(index);

        // Then
        assertThat(fibonacci7th).isEqualTo(13);
    }

}///:~