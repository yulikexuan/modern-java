//: com.yulikexuan.effectivejava.model.design.InstrumentedSetTest.java


package com.yulikexuan.effectivejava.model.design;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test Add Methods of Instrumented Forwarding Set Class - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InstrumentedSetTest {

    private InstrumentedSet<String> snack;

    @BeforeEach
    void setUp() {
        this.snack = InstrumentedSet.of(Sets.newHashSet());
    }

    @Test
void test_Given_Collection_Of_Names_When_Calling_AddAll_Then_Get_Correct_AddingCount() {

        // Given
        List<String> names = List.of("Cake", "Bagel", "Chips");
        int sizeOfAddedElement = names.size();

        // When
        this.snack.addAll(names);
        int actualAddingCount = this.snack.getAddingCount();

        // Then
        assertThat(actualAddingCount).isEqualTo(sizeOfAddedElement);
    }

}///:~