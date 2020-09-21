//: com.yulikexuan.effectivejava.generics.InitialGenerifiedStackTest.java


package com.yulikexuan.effectivejava.generics;


import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Generified Stack Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InitialGenerifiedStackTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_No_Cast_Need_For_Generified_Stack_Any_More() {

        // Given
        InitialGenerifiedStack<String> stringStack = InitialGenerifiedStack.of();

        IntStream.range(0, 3)
                .mapToObj(RandomStringUtils::randomAlphanumeric)
                .forEach(stringStack::push);

        // When & Then
        while (!stringStack.isEmpty()) {
            assertThat(stringStack.pop()).isInstanceOf(String.class);
        }
    }

    @Test
    void test_Push_All_Elements_From_Integer_List_To_Number_Type_Stack() {

        // Given
        InitialGenerifiedStack<Number> numberStack = InitialGenerifiedStack.of();

        List<Integer> numberList = Lists.newArrayList(
                0, 1, 2, 3, 4, 5, 6, 7);

        // When
        numberStack.pushAll(numberList);
    }

}///:~