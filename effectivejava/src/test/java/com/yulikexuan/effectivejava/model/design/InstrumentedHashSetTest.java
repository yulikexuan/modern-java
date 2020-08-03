//: com.yulikexuan.effectivejava.model.design.InstrumentedHashSetTest.java


package com.yulikexuan.effectivejava.model.design;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test Add Methods of Instrumented HashSet Class - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InstrumentedHashSetTest {

    private InstrumentedHashSet<String> snack;

    @BeforeEach
    void setUp() {
         this.snack = new InstrumentedHashSet();
    }

    @Test
    void test_Given_Collection_Of_Names_When_Calling_AddAll_Then_Get_Wrong_AddingCount() {

        // Given
        List<String> names = List.of("Cake", "Bagel", "Chips");
        int sizeOfAddedElement = names.size();

        // When
        this.snack.addAll(names);
        int actualAddingCount = this.snack.getAddingCount();

        // Then
        assertThat(actualAddingCount).isNotEqualTo(sizeOfAddedElement);
    }

}///:~