//: com.yulikexuan.effectivejava.boxing.BoxedPrimitivesTest.java

package com.yulikexuan.effectivejava.boxing;


import org.junit.jupiter.api.*;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Boxed Primitives - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BoxedPrimitivesTest {

    private int value;

    private ThreadLocalRandom random;

    @BeforeEach
    void setUp() {
        this.random = ThreadLocalRandom.current();
        this.value = this.random.nextInt(100, 200);
    }


    @Test
    void two_Integers_Which_Have_The_Same_Value_Should_Have_Different_Identities() {

        // Given
        Integer int1 = Integer.valueOf(this.value);
        Integer int2 = Integer.valueOf(this.value);

        // When & Then
        assertThat(int1 == int2).isFalse();
    }

}///:~