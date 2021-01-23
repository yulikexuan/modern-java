//: com.yulikexuan.concurrency.thread.immutability.ThreeStoogesTest.java

package com.yulikexuan.concurrency.thread.immutability;


import org.junit.jupiter.api.*;

import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;


@DisplayName("Test ThreeStoogesTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ThreeStoogesTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_ThreeStooges_Should_Be_Immutable() {
        assertImmutable(ThreeStooges.class);
    }

}///:~