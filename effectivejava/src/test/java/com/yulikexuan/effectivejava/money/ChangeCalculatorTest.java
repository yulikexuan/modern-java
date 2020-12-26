//: com.yulikexuan.effectivejava.money.ChangeCalculatorTest.java

package com.yulikexuan.effectivejava.money;


import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Money Change Calculation - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ChangeCalculatorTest {

    @Test
    void the_Calculated_Change_Left_With_Double_After_Buying_Candies_Is_Wrong() {

        // Given
        double funds = 1.00d;

        // When
        ImmutablePair<Double, Integer> left = ChangeCalculator.calculate(funds);

        // Then
        assertThat(left.left).isNotZero();
        assertThat(left.right).isNotEqualTo(4);
    }

    @Test
    void the_Calculated_Change_Left_With_BigDecimal_After_Buying_Candies_Should_Be_Correct() {

        // Given
        BigDecimal funds = new BigDecimal("1.00");

        // When
        ImmutablePair<BigDecimal, Integer> left =
                ChangeCalculator.calculateWithBigDecimal(funds);

        // Then
        assertThat(left.left).isZero();
        assertThat(left.right).isEqualTo(4);
    }

}///:~