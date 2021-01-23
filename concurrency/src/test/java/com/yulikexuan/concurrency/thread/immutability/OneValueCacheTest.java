//: com.yulikexuan.concurrency.thread.immutability.OneValueCacheTest.java

package com.yulikexuan.concurrency.thread.immutability;


import org.junit.jupiter.api.*;

import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;


@DisplayName("OneValueCache Immutability Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OneValueCacheTest {

    @Test
    void name() {
        assertImmutable(OneValueCache.class);
    }
}///:~