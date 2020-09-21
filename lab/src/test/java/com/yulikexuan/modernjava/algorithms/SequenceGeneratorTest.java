//: com.yulikexuan.modernjava.algorithms.SequenceGeneratorTest.java


package com.yulikexuan.modernjava.algorithms;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


class SequenceGeneratorTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Generating_Even_Numbers() {

        // Given
        int size = 10;
        Integer[] evenNumArray = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18};

        // When
        List<Integer> evenNumbers = SequenceGenerator.generateEvenNumbers(size);

        // Then
        assertThat(evenNumbers).containsExactly(evenNumArray);
    }

    @Test
    void test_Generating_Even_Numbers_Under_The_Maximum() {

        // Given
        int maximum = 17;
        Integer[] evenNumArray = {0, 2, 4, 6, 8, 10, 12, 14, 16};

        // When
        List<Integer> evenNumbers = SequenceGenerator
                .generateEvenNumbersLessThan(maximum);

        // Then
        assertThat(evenNumbers).containsExactly(evenNumArray);
    }

    @Test
    @Disabled
    void test_Print_Fibonacci_Numbers() {
        SequenceGenerator.printFibonacciNumbers(7);
    }

    @Test
    void test_Generate_Some_Random_Numbers() {

        // Given
        int size = 7;

        // When
        List<Integer> randomNums = SequenceGenerator.generateRandomNumbers(size);

        // Then
        assertThat(randomNums).hasSize(size);
    }

    @Test
    void test_Generate_Fabonacci_Numbers_With_IntSupplier() {

        // Given
        int size = 7;

        // When
        IntStream.generate(SequenceGenerator.FIBONACCI_NUMBER_SUPPLIER)
                .limit(size)
                .forEach(n -> System.out.println(n));

    }

}///:~