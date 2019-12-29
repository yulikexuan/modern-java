//: com.yulikexuan.modernjava.fp.FactorialCalaulatorTest.java


package com.yulikexuan.modernjava.fp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;


class FactorialCalaulatorTest {

    private static final long N = 17;

    private Instant start;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test tail recursively factorial calculation - ")
    void test_Tail_Recursive_Factorial() {

        // Given
        this.start = Instant.now();
        TailRecursiveFactorialCalaulator calc =
                new TailRecursiveFactorialCalaulator();

        // When
        long factorialStr = calc.calculateFactorial(N);
        long duration = Duration.between(this.start, Instant.now()).toMillis();

        // Then
        System.out.printf(">>>>>>> Tail recursively calculated factorial of %d " +
                        "in %d msec : %d",
                N, duration, factorialStr);
    }

    @Test
    @DisplayName("Test recursive factorial calculation - ")
    void test_Recursive_Factorial() {

        // Given
        this.start = Instant.now();
        RecursiveFactorialCalaulator recCalc = new RecursiveFactorialCalaulator();

        // When
        long factorialRec = recCalc.calculateFactorial(N);
        long duration = Duration.between(this.start, Instant.now()).toMillis();

        // Then
        System.out.printf(">>>>>>> Recursively calculated factorial of %d " +
                        "in %d msec : %d",
                N, duration, factorialRec);
    }

    @Test
    @DisplayName("Test streamly factorial calculation - ")
    void test_Factorial_Calculation_With_Stream() {

        // Given
        this.start = Instant.now();
        StreamFactorialCalaulator strCalc = new StreamFactorialCalaulator();

        // When
        long factorialStr = strCalc.calculateFactorial(N);
        long duration = Duration.between(this.start, Instant.now()).toMillis();

        // Then
        System.out.printf(">>>>>>> Calculated factorial of %d with Stream " +
                        "in %d msec : %d",
                N, duration, factorialStr);
    }

}///:~