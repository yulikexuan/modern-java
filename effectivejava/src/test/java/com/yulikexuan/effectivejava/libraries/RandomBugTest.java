//: com.yulikexuan.effectivejava.libraries.RandomBugTest.java

package com.yulikexuan.effectivejava.libraries;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test Generating Random Numbers Badly - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RandomBugTest {

    @Test
    void test_How_Many_Random_Numbers_Fell_In_The_Lower_Half() {

        // Given
        int n = 2 * (Integer.MAX_VALUE / 3);
        int low = 0;

        for (int i = 0; i < 1_000_000; i++) {
            if (RandomBug.random(n) < n/2) {
                low++;
            }
        }

        assertThat(low).isGreaterThan(60_000);
    }

    @Test
    void test_The_Abs_Of_Integer_Min_Value_Is_Still_Negative() {

        // Given & When
        int positiveMin = Math.abs(Integer.MIN_VALUE);

        // Then
        assertThat(positiveMin).isEqualTo(Integer.MIN_VALUE);
    }

}///:~