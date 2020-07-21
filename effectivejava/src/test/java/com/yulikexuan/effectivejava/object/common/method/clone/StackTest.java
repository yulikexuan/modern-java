//: com.yulikexuan.effectivejava.object.common.method.clone.StackTest.java


package com.yulikexuan.effectivejava.object.common.method.clone;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Equals Methods of SubClasses - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StackTest {

    private static final int STRING_LENGTH = 30;

    private Stack stack;

    @BeforeEach
    void setUp() {
        this.stack = new Stack();
        IntStream.range(0, 10)
                .forEach(i -> this.stack.push(
                        RandomStringUtils.randomAlphanumeric(30)));
    }

    @Test
    void test_Cloning_Object_With_Immutable_Type_Array_As_Its_Content() {

        // When
        Stack stackCopy = this.stack.clone();

        // Then
        assertThat(stackCopy).isNotSameAs(stack);
        assertThat(stack.getSize()).isEqualTo(stackCopy.getSize());
        while (!stack.isEmpty()) {
            assertThat(stack.pop()).isSameAs(stackCopy.pop());
        }
    }

}///:~