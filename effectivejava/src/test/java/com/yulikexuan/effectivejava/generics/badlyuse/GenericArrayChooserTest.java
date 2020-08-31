//: com.yulikexuan.effectivejava.generics.badlyuse.GenericArrayChooserTest.java


package com.yulikexuan.effectivejava.generics.badlyuse;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Generic Array Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GenericArrayChooserTest {

    private GenericArrayChooser<String> nameChooser;

    @Test
    void test_Generic_Array_Works_With_Compile_Warning() {

        // Given
        Collection<String> names = List.of(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));

        this.nameChooser = new GenericArrayChooser<>(names);

        // When
        String chosenId = this.nameChooser.choice();

        // Then
        assertThat(names).contains(chosenId);
    }

}///:~