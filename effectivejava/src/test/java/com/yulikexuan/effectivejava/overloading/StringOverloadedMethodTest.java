//: com.yulikexuan.effectivejava.overloading.StringOverloadedMethodTest.java

package com.yulikexuan.effectivejava.overloading;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.SplittableRandom;


@DisplayName("Test overloaded methods of String - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StringOverloadedMethodTest {

    private String data;

    private SplittableRandom random;


    @BeforeEach
    void setUp() {
        this.data = RandomStringUtils.randomAlphanumeric(30);
    }

    @Test
    void test_Content_Equal_Methods() {

        // Given
        StringBuffer stringBuilder = new StringBuffer(data);

        // When
        this.data.contentEquals(stringBuilder);

    }

}///:~