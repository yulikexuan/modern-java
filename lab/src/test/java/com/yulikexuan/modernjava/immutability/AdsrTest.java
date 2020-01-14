//: com.yulikexuan.modernjava.immutability.AdsrTest.java


package com.yulikexuan.modernjava.immutability;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Immutability Test for Adsr")
class AdsrTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test the immutability of name field - ")
    void test_The_Name_Immutability_Of_Adsr() {

        // Given
        StringBuilder nameBuilder = new StringBuilder("a1 ");
        Adsr a1 = Adsr.builder().name(nameBuilder)
                .attack(5)
                .decay(7)
                .build();
        Adsr a2 = a1.getAdsr();

        // When
        String a1_name = a1.getName().toString();
        nameBuilder.append("alter the name ");
        String a1_new_name = a1.getName().toString();

        // Then
        assertThat(a1_name)
                .as("The name of an Adsr instance should be " +
                        "not able to changed.")
                .isEqualTo(a1_new_name);

    }

}///:~