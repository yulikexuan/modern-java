//: com.yulikexuan.effectivejava.varargs.NonSingleVarargsTest.java

package com.yulikexuan.effectivejava.varargs;


import org.junit.jupiter.api.*;

import java.util.SplittableRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test NonSingleVarargs - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NonSingleVarargsTest {

    private SplittableRandom random;

    @BeforeEach
    void setUp() {
        this.random = new SplittableRandom(System.currentTimeMillis());
    }

    @Test
    void test_Only_Varargs_Can_Fail_In_Runtime() {
        assertThatThrownBy(() -> NonSingleVarargs.findMin())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void test_Only_One_Arg() {

        // Given
        int firstInt = this.random.nextInt(0, 100);

        // When
        int min = NonSingleVarargs.min(firstInt);
        int min2 = NonSingleVarargs.min2(firstInt);

        // Then
        assertThat(min).isEqualTo(firstInt);
        assertThat(min2).isEqualTo(min);
    }

    @Test
    void test_One_Arg_With_Varargs() {

        // Given
        int firstArg = this.random.nextInt(0, 100);

        int arg0 = this.random.nextInt(0, 100);
        int arg1 = this.random.nextInt(0, 100);
        int arg2 = this.random.nextInt(0, 100);

        // When
        int min = NonSingleVarargs.min(firstArg, arg0, arg1, arg2);
        int min2 = NonSingleVarargs.min2(firstArg, arg0, arg1, arg2);

        // Then
        assertThat(min).isEqualTo(min2);
    }

}///:~