//: com.yulikexuan.effectivejava.object.construction.EliminateMemoryLeakTest.java


package com.yulikexuan.effectivejava.object.construction;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


class EliminateMemoryLeakTest {

    static final int INIT_NUMBER_OF_ELEMENTS = 7;

    private StackWithMemoryLeak stack;

    @BeforeEach
    void setUp() {
        this.stack = new StackWithMemoryLeak();
        IntStream.range(0, INIT_NUMBER_OF_ELEMENTS)
                .mapToObj(i -> RandomStringUtils.randomAlphanumeric(10))
                .forEach(obj -> this.stack.push(obj));
    }

    @Test
    void test_Given_Pop_Actions_Then_Eliminate_Popped_Objects() {

        // Given
        int popTimes = 3;
        int startSize = this.stack.getSize();

        IntStream.range(0, popTimes)
                .forEach(i -> {
                    this.stack.pop();
                    assertThat(this.stack.getPoppedElement()).isNull();
                });

        // When
        int finalSize = this.stack.getSize();

        // Then
        assertThat(startSize).isEqualTo(INIT_NUMBER_OF_ELEMENTS);
        assertThat(finalSize).isEqualTo(INIT_NUMBER_OF_ELEMENTS - popTimes);
    }

}///:~