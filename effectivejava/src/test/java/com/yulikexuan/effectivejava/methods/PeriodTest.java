//: com.yulikexuan.effectivejava.methods.PeriodTest.java

package com.yulikexuan.effectivejava.methods;


import org.junit.jupiter.api.*;
import org.mutabilitydetector.unittesting.AllowedReason;

import java.util.Date;

import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;


@DisplayName("Test PeriodTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PeriodTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Period_Class_Should_Be_Immutable() {
        assertInstancesOf(Period.class, areImmutable(),
                AllowedReason.provided(Date.class).isAlsoImmutable());
    }

}///:~