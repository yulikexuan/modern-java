//: com.yulikexuan.concurrency.design.PointTest.java

package com.yulikexuan.concurrency.design;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;


@DisplayName("Test PointTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PointTest {

    @Test
    void test_Point_Class_Should_Be_Immutable() {
        assertImmutable(Point.class);
    }

}///:~