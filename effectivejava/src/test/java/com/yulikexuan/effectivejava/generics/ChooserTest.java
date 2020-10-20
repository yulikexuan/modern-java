//: com.yulikexuan.effectivejava.generics.ChooserTest.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


class ChooserTest {

    private Chooser<Number> chooser;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Able_To_Accept_Integers() {

        // Given
        List<Integer> ints = IntStream.range(0, 10).boxed()
                .collect(ImmutableList.toImmutableList());

        // When
        this.chooser = new Chooser<>(ints);

        // Then
        assertThat(this.chooser.choice()).isNotNull()
                .isInstanceOf(Integer.class);
    }

}///:~