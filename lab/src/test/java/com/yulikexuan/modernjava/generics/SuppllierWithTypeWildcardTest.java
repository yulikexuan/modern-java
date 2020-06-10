//: com.yulikexuan.modernjava.generics.SuppllierWithTypeWildcardTest.java


package com.yulikexuan.modernjava.generics;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.jupiter.api.*;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test value cast when using wildcard in Supplier - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SuppllierWithTypeWildcardTest {

    private RandomDataGenerator random;
    private Supplier<?> valueSupplier;


    @BeforeEach
    void setUp() {
        this.random = new RandomDataGenerator();
        this.valueSupplier = () -> this.random.nextInt(0, Integer.MAX_VALUE);
    }

    @Test
    void test_Given_Long_Supplier_When_Fetching_Integer_Then_Cast_Twice() {

        // Given
        Integer valueObj = (Integer) this.valueSupplier.get();

        // When
        int value = valueObj.intValue();

        // Then
        assertThat(value).isGreaterThanOrEqualTo(0);
    }

}///:~