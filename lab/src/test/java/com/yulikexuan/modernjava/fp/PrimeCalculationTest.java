//: com.yulikexuan.modernjava.fp.PrimeCalculationTest.java


package com.yulikexuan.modernjava.fp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PrimeCalculationTest {


    @BeforeEach
    void setUp() {
    }

    @ParameterizedTest
    @DisplayName("Test if method isPrime works or not - ")
    @ValueSource(ints = {1, 2, 3, 5, 7, 11, 13, 17, 19})
    void test_Primes(int number) {

        // When
        boolean isPrime = PrimeCalculation.isPrime(number);

        // Then
        assertThat(isPrime)
                .as("The input should be prime but is %d")
                .isTrue();
    }

    @ParameterizedTest
    @DisplayName("Test if method isPrime works or not - ")
    @ValueSource(ints = {10, 20})
    void test_Prime_Stream(int number) {

        // When

        // Then
        System.out.println(PrimeCalculation.primes(number).collect(toList()));
    }

    @Test
    @DisplayName("Test failed recursive stream - ")
    void test_Recursively_Compute_Primes() {
        // Then
        assertThatThrownBy(() -> PrimeCalculation.recursivelyPrimes(
                        PrimeCalculation.getNumbers()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("stream has already been operated upon or closed");
    }

}///:~