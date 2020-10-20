//: com.yulikexuan.effectivejava.generics.BoundedWildcardsStackTest.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.ImmutableList;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("Test bounded wildcards for bulk methods- ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BoundedWildcardsStackTest {

    private BoundedWildcardsStack<Number> numberStack;

    private List<Integer> integers;

    @BeforeEach
    void setUp() {
        this.numberStack = new BoundedWildcardsStack<>();
        this.integers = IntStream.range(0, 10).boxed()
                .collect(ImmutableList.toImmutableList());
    }

    @Test
    void test_Adding_Integers() {

        // Given

        // When
        this.numberStack.pushAll(integers);

        // Then
        assertThat(this.numberStack.pop()).isEqualTo(9);
    }

    @Test
    void test_Poping_Objects() {

        // Given
        this.numberStack.pushAll(integers);
        Collection<Object> dst = Lists.newArrayList();

        // When
        this.numberStack.popAll(dst);

        // Then
        assertThat(dst.size()).isEqualTo(this.integers.size());
        assertThat(dst).containsExactly(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
    }

}///:~