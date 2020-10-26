//: com.yulikexuan.effectivejava.generics.varargs.PickTwoTest.java


package com.yulikexuan.effectivejava.generics.varargs;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test Exposing Varargs Array is Dangerous - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PickTwoTest {

    @Test
    void test_Cast_Varargs_Array_Is_Dangerous() {

        assertThatThrownBy(() -> {
                String[] selectedNames = PickTwo.pickTwo(
                        RandomStringUtils.randomAlphabetic(7),
                        RandomStringUtils.randomAlphabetic(7),
                        RandomStringUtils.randomAlphabetic(7));
        }).isInstanceOf(ClassCastException.class);
    }

}///:~