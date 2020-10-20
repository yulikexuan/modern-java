//: com.yulikexuan.effectivejava.generics.RecursiveTypeBoundTest.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test Recursive Type Bound with Wildcard Types - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RecursiveTypeBoundTest {

    private ThreadLocalRandom random;
    private List<Integer> integers;

    @BeforeEach
    void setUp() {
        this.random = ThreadLocalRandom.current();
        this.integers = IntStream.range(0, 10)
                .mapToObj(i -> this.random.nextInt(0, 10))
                .collect(ImmutableList.toImmutableList());

    }

    @Test
    void test_Recursive_Type_Bound_Without_Wildcard() {

        RecursiveTypeBound.max(this.integers)
                .orElseThrow();
    }

    @Test
    void test_Recursive_Type_Bound_With_Wildcard() {

        // Given & When
        Number number = RecursiveTypeBound.maxFree(this.integers);

        // Then
        assertThat(number).isInstanceOf(Integer.class);
    }

    @Disabled
    @Test
    void test_Recursive_Type_Bound_Without_Wildcard_Difficulties() {

        // Given
        List<ScheduledFuture<?>> futures = List.of();

        // When

        /*
         * ScheduledFuture is not Comparable
         * However, ScheduledFuture's super class, Delayed, is Comparable
         */
        // ScheduledFuture<Object> maxFuture = RecursiveTypeBound.max(futures);
    }

    @Test
void test_Recursive_Type_Bound_With_Wildcard_Flexibilities() {

        // Given
        List<ScheduledFuture<?>> futures = List.of();

        // When

        /*
         * ScheduledFuture is not Comparable
         * However, ScheduledFuture's super class, Delayed, is Comparable
         */
        // Delayed f = RecursiveTypeBound.maxFree(futures);
        assertThatThrownBy(() -> RecursiveTypeBound.maxFree(futures))
                .isInstanceOf(IllegalArgumentException.class);
    }

}///:~