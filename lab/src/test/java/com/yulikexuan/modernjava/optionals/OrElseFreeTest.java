//: com.yulikexuan.modernjava.optionals.OrElseFreeTest.java


package com.yulikexuan.modernjava.optionals;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Avoid orElse Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrElseFreeTest {

    private OptionalRecipes optionalRecipes;

    @BeforeEach
    void setUp() {
        this.optionalRecipes = OptionalRecipes.of();
    }

    @Test
    void test_Given_Id_When_Search_Secret() {

        // Given
        int id = 0;

        // When
        String actualSecret = this.optionalRecipes.findSecret(id);

        // Then
        assertThat(actualSecret).isNotNull();
        assertThat(optionalRecipes.getDbCount()).isZero();
        assertThat(optionalRecipes.getCacheCount()).isOne();
    }

}///:~