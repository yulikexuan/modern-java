//: com.yulikexuan.effectivejava.generics.GenericSingletonFactoryTest.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Generic Singleton Factory Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GenericSingletonFactoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Given_String_Array_When_Fetching_Identity_Then_Fetch_With_IDENTITY_FUNC() {

        // Given
        String[] material = {"jute", "hemp", "nylon"};

        UnaryOperator<String> nameIdentityFunc =
                GenericSingletonFactory.identityFunction();

        // When
        List<String> materialNames = Arrays.stream(material)
                .map(nameIdentityFunc)
                .collect(ImmutableList.toImmutableList());

        // Then
        assertThat(materialNames).containsExactly(material);
    }

    @Test
    void test_Given_String_Array_When_Fetching_Identity_Then_Fetch_With_Function_identity() {

        // Given
        String[] material = {"jute", "hemp", "nylon"};

        Function<String, String> nameIdentityFunc = Function.identity();

        // When
        List<String> materialNames = Arrays.stream(material)
                .map(nameIdentityFunc)
                .collect(ImmutableList.toImmutableList());

        // Then
        assertThat(materialNames).containsExactly(material);
    }

}///:~