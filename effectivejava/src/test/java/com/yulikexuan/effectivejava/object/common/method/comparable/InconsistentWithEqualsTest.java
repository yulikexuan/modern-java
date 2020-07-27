//: com.yulikexuan.effectivejava.object.common.method.comparable.InconsistentWithEqualsTest.java


package com.yulikexuan.effectivejava.object.common.method.comparable;


import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Equals Methods of SubClasses - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class InconsistentWithEqualsTest {

    @Test
    void test_BigDecimal() {

        // Given
        Set<BigDecimal> priceSet = Sets.newHashSet();
        Set<BigDecimal> priceSortedSet = Sets.newTreeSet();

        BigDecimal price_1 = new BigDecimal("1.0");
        BigDecimal price_2 = new BigDecimal("1.00");

        // When
        priceSet.add(price_1);
        priceSet.add(price_2);

        priceSortedSet.add(price_1);
        priceSortedSet.add(price_2);

        // Then
        assertThat(price_1.equals(price_2)).isFalse();
        assertThat(price_1.compareTo(price_2)).isEqualTo(0);
        assertThat(priceSet.size()).isEqualTo(2);
        assertThat(priceSortedSet.size()).isEqualTo(1);
    }

}///:~