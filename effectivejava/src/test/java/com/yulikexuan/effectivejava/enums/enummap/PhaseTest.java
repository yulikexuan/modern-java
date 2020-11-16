//: com.yulikexuan.effectivejava.enums.enummap.PhaseTest.java

package com.yulikexuan.effectivejava.enums.enummap;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.yulikexuan.effectivejava.enums.enummap.Phase.FragileTransition;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test PhaseTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PhaseTest {

    @BeforeEach
    void setUp() {
    }

    @ParameterizedTest
    @CsvSource(value = {
            "SOLID:LIQUID:MELT",
            "SOLID:GAS:SUBLIME",
            "LIQUID:SOLID:FREEZE",
            "LIQUID:GAS:BOIL",
            "GAS:SOLID:DEPOSIT",
            "GAS:LIQUID:CONDENSE"},
            delimiter = ':')
    void test_Given_Two_Phases_Then_Knows_Fragile_Transaction(
            Phase from, Phase to, FragileTransition expectedTransition) {

        // When
        FragileTransition transition = FragileTransition.from(from, to);

        // Then
        assertThat(transition).isSameAs(expectedTransition);
    }

}///:~