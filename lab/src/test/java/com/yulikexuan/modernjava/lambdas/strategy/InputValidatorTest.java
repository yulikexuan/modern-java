//: com.yulikexuan.modernjava.lambdas.strategy.InputValidatorTest.java


package com.yulikexuan.modernjava.lambdas.strategy;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class InputValidatorTest {

    private InputValidator numericValidator;
    private InputValidator lowerCaseValidator;

    @BeforeEach
    void setUp() {
        this.numericValidator = InputValidator.of(
                input -> input.matches("\\d+"));
        this.lowerCaseValidator = InputValidator.of(
                input -> input.matches("[a-z]+"));
    }

    @Test
    void test_Given_A_String_Containing_All_Digits_Then_Validate_As_Input() {

        // Given
        String input_1 = String.valueOf(Long.MAX_VALUE);

        // When
        boolean isActualDigits = this.numericValidator.validate(input_1);
        boolean isActualLowerCase = this.lowerCaseValidator.validate(input_1);

        // Then
        assertThat(isActualDigits).isTrue();
        assertThat(isActualLowerCase).isFalse();
    }

}///:~