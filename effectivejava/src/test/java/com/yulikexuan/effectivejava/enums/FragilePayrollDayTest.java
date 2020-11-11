//: com.yulikexuan.effectivejava.enums.FragilePayrollDayTest.java


package com.yulikexuan.effectivejava.enums;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test switch statement is not good for constant-specific behaviour of enum - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FragilePayrollDayTest {

    public static final String UNFAIR_PAY = "Unfail Pay!!!";

    @Test
    void test_Vacation_Pay_Should_Not_Equals_To_Ordinary_Workday() {

        // When
        assertThatThrownBy(() -> {
            int overtimePay = FragilePayrollDay.VACATION_DAY.pay(240,
                    10);
            int workDayPay = FragilePayrollDay.MONDAY.pay(240,
                    10);

            if (overtimePay == workDayPay) {
                throw new IllegalStateException(UNFAIR_PAY);
            }
        }).isInstanceOf(IllegalStateException.class)
                .hasMessage(UNFAIR_PAY);
    }

}///:~