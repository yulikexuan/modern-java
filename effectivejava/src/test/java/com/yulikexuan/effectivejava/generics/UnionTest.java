//: com.yulikexuan.effectivejava.generics.UnionTest.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Generic Method with Wildcard Types - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UnionTest {

    @Test
    void test_Given_Two_Sets_Of_Different_Number_Types_Then_Union() {

        // Given
        Set<Integer> integers = IntStream.range(0, 10).boxed()
                .collect(ImmutableSet.toImmutableSet());

        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<Double> doubles = DoubleStream.generate(random::nextDouble)
                .limit(5).boxed().collect(ImmutableSet.toImmutableSet());

        // When
        Set<Number> numbers = Union.union(integers, doubles);

        // Then
        assertThat(numbers.size()).isEqualTo(integers.size() + doubles.size());
    }

}///:~