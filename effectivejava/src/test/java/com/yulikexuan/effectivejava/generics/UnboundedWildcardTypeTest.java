//: com.yulikexuan.effectivejava.generics.UnboundedWildcardTypeTest.java


package com.yulikexuan.effectivejava.generics;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Unbounded Wildcard - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UnboundedWildcardTypeTest {

    private Set<String> set1;
    private Set<Integer> set2;

    @BeforeEach
    void setUp() {
        set1 = Set.of(
                RandomStringUtils.randomAlphanumeric(30),
                RandomStringUtils.randomAlphanumeric(30),
                RandomStringUtils.randomAlphanumeric(30));
        set2 = Set.of(
                RandomUtils.nextInt(),
                RandomUtils.nextInt(),
                RandomUtils.nextInt());
    }

    @Test
    void test_No_Common_Element_Between_Set1_And_Set2() {

        // When
        long count = UnboundedWildcardType.countElementsInCommon(set1, set2);

        // Then
        assertThat(count).isZero();
    }

    @Test
    void test_Instance_Of_Operator() {

        // Given
        Set<Integer> intSet = Set.of();
        List<String> strList = List.of();

        // When
        Set<?> set_1 = UnboundedWildcardType.castToSet(intSet);
        Set<?> set_2 = UnboundedWildcardType.castToSet(strList);

        // Then
        assertThat(set_1).isNotNull();
        assertThat(set_2).isNull();
    }

}///:~