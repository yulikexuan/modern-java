//: com.yulikexuan.effectivejava.generics.InitialGenerifiedStackTest.java


package com.yulikexuan.effectivejava.generics;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Generified Stack Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InitialGenerifiedStackTest {

    private InitialGenerifiedStack<String> stringStack;

    @BeforeEach
    void setUp() {
        this.stringStack = InitialGenerifiedStack.of();
    }

    @Test
    void test_No_Cast_Need_For_Generified_Stack_Any_More() {

        // Given
        IntStream.range(0, 3)
                .mapToObj(RandomStringUtils::randomAlphanumeric)
                .forEach(this.stringStack::push);

        // When & Then
        while (!this.stringStack.isEmpty()) {
            assertThat(this.stringStack.pop()).isInstanceOf(String.class);
        }
    }

}///:~