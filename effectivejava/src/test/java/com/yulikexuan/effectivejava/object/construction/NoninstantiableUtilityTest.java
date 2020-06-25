//: com.yulikexuan.effectivejava.object.construction.NoninstantiableUtilityTest.java


package com.yulikexuan.effectivejava.object.construction;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
@DisplayName("Test non-instantiability - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NoninstantiableUtilityTest {

    @Slf4j
    static class NoninstantiableUtility {

        // Suppress default constructor for non-instantiablitity
        private NoninstantiableUtility() {
            throw new AssertionError();
        }

        public static BigDecimal getRandomValue() {
            return BigDecimal.valueOf(RandomUtils.nextDouble(10, 100));
        }
    }

    @Test
    void test_Not_Possible_To_Create_Any_Instance_Of_NoninstantiableUtility() {
        // When & Then
        assertThatThrownBy(() -> { new NoninstantiableUtility(); })
                .isInstanceOf(AssertionError.class);
    }

}///:~