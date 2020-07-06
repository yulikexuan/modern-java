//: com.yulikexuan.effectivejava.object.common.method.equals.CaseInsensitiveStringTest.java


package com.yulikexuan.effectivejava.object.common.method.equals;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test Equals Method of CaseInsensitiveString - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CaseInsensitiveStringTest {

    private String testString;
    private CaseInsensitiveString caseInsensitiveString;
    private AsymmetricaCaseInsensitiveString asymmetricaCaseInsensitiveString;

    @BeforeEach
    void setUp() {
        this.testString = "polish";
    }

    @Test
    void test_Given_AsymmetricaCaseInsensitiveString_When_Compare_To_Other_String_Then_True() {

        // Given
        this.asymmetricaCaseInsensitiveString =
                AsymmetricaCaseInsensitiveString.of("Polish");

        // When
        boolean equalsToString = asymmetricaCaseInsensitiveString.equals(
                this.testString);

        // Then
        assertThat(equalsToString).isTrue();
    }

    @Test
    void test_Given_A_String_When_Compare_To_AsymmetricaCaseInsensitiveString_Then_Fail_Symmetry() {

        // Given
        this.asymmetricaCaseInsensitiveString =
                AsymmetricaCaseInsensitiveString.of("Polish");

        // When
        boolean isSymmetry = this.testString.equals(this.asymmetricaCaseInsensitiveString);

        // Then
        assertThat(isSymmetry).isFalse();
    }

    @Test
    void test_Given_CaseInsensitiveString_When_Comnpare_To_String_Then_Failed() {

        // Given
        this.caseInsensitiveString = CaseInsensitiveString.of("Polish");

        // When
        boolean direction_1 = this.caseInsensitiveString.equals(this.testString);
        boolean direction_2 = this.testString.equals(this.caseInsensitiveString);

        boolean isSymmetry = direction_1 == direction_2;

        // Then
        assertThat(direction_1).isFalse();
        assertThat(direction_2).isFalse();
        assertThat(isSymmetry).isTrue();
    }

}///:~