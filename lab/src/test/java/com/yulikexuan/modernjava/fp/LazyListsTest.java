//: com.yulikexuan.modernjava.fp.LazyListsTest.java


package com.yulikexuan.modernjava.fp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("Functional Lazy List Test - ")
class LazyListsTest {

    static final int SEED = 2;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test a LazyList of Integer - ")
    void test_Integer_Lazy_List() {

        // Given
        ILazyList<Integer> numberList = ILazyList.numberLazyList(
                SEED, i -> ++i, 10);

        // When
        List<Integer> allNumbers = ILazyList.getAll(numberList);

        // Then
        assertThat(allNumbers)
                .as("Should contain a sequence of intgers which " +
                        "starts at 2")
                .containsExactly(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    }

    @Test
    @DisplayName("Test 10 prime numbers - ")
    void test_Prime_Lazy_List() {

        // Given
        ILazyList<Integer> primeList = LazyLists.getPrimeList(4);

        // When
        List<Integer> primes = ILazyList.getHeads(primeList, 10);


        // Then
        assertThat(primes)
                .as("Should contain 10 prime numbers starting from 2")
                .containsExactly(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
    }

}///:~