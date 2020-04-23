//: com.yulikexuan.modernjava.arrays.ArraysTest.java


package com.yulikexuan.modernjava.arrays;


import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

@DisplayName("Arrays Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ArraysTest {

    static final int ARRAY_SIZE = 17;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Given_An_Array_Then_Fill_Fibonacci_Elements() {

        // Given
        final int length = ARRAY_SIZE;
        long[] fibonacciArr = new long[length];
        long[] expectedArr = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144,
                233, 377, 610, 987};

        // When
        Arrays.setAll(fibonacciArr, (int index) -> {
            if (index == 0) {
                return 0L;
            } else if (index == 1) {
                return 1L;
            }
            return fibonacciArr[index - 2] + fibonacciArr[index - 1];
        });

        // Then
        assertThat(fibonacciArr).containsExactly(expectedArr);
    }

    @Test
    void test_Given_An_Array_Then_Prefix_Each_Element() {

        // Given
        long[] nums = new long[ARRAY_SIZE];
        Arrays.fill(nums, 1L);

        // When
        Arrays.parallelPrefix(nums, Long::sum);

        // Then
        Arrays.stream(nums).forEach(i -> System.out.print("  " + i));
        System.out.println();
    }

}///:~