//: com.yulikexuan.jdk.ocp2.generic.VeterinarianTest.java

package com.yulikexuan.jdk.ocp2.generic;


import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test Veterinarian with Generic - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VeterinarianTest {

    private Veterinarian veter;

    @BeforeEach
    void setUp() {
        this.veter = new Veterinarian();
    }

    @Test
    void dog_Array_Cannot_Accept_Cat_In_Runtime() {

        // Given
        Dog[] dogs = new Dog[] { new Dog(), new Dog() };

        // When & Then
        assertThatThrownBy(() -> this.veter.addAnimalButNotDog(dogs))
                .isExactlyInstanceOf(ArrayStoreException.class);
    }

    @Test
    void collection_Of_Super_Animal_Accept_Dog_And_Cat() {

        // Given
        List<? super Animal> animals = Lists.newArrayList(new Dog(), new Dog());

        // When
        this.veter.addAnimalGeneric(animals);

        // Then
        assertThat(animals.get(2)).isInstanceOf(Cat.class);
    }

}///:~