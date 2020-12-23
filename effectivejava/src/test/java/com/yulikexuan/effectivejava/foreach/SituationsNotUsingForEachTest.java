//: com.yulikexuan.effectivejava.foreach.SituationsNotUsingForEachTest.java

package com.yulikexuan.effectivejava.foreach;


import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.SplittableRandom;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("Test Situations Not for ForEach Loops - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SituationsNotUsingForEachTest {

    private List<Integer> numbers;
    private SplittableRandom random;

    @BeforeEach
    void setUp() {
        this.random = new SplittableRandom(System.currentTimeMillis());
        this.numbers = Lists.newArrayList(
                this.random.nextInt(0, 100),
                this.random.nextInt(0, 100),
                this.random.nextInt(0, 100),
                this.random.nextInt(0, 100),
                this.random.nextInt(0, 100),
                this.random.nextInt(0, 100),
                this.random.nextInt(0, 100));
    }

    @Test
    void given_An_Array_Of_Num_Then_Remove_Even_Nums_With_ForEach() {

        // Given

        // When
        assertThatThrownBy(() ->
                SituationsNotUsingForEach.removeElementsUsingForEach(
                this.numbers, SituationsNotUsingForEach::isEven))
                .isInstanceOf(ConcurrentModificationException.class);
    }

    @Test
    void given_An_Array_Of_Num_Then_Remove_Even_Nums_Without_ForEach() {

        // Given

        // When
        SituationsNotUsingForEach.removeElements(this.numbers,
                SituationsNotUsingForEach::isEven);

        // Then
        assertThat(this.numbers.stream()
                .anyMatch(SituationsNotUsingForEach::isEven))
                .isFalse();
    }

}///:~