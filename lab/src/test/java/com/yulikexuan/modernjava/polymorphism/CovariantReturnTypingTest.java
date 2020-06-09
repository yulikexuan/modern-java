//: com.yulikexuan.modernjava.polymorphism.CovariantReturnTypingTest.java


package com.yulikexuan.modernjava.polymorphism;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test Covariant Return Typing Support - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CovariantReturnTypingTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Covariant_Return_Typing() {

        // Given
        CoolBuilder gBuilder = new CoolBuilder();

        // When
        CoolBuilder cBuilder = gBuilder.self();

        // Then
        assertThat(cBuilder).isExactlyInstanceOf(CoolBuilder.class);
    }

}///:~