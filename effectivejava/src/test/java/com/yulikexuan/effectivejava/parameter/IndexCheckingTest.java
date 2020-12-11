//: com.yulikexuan.effectivejava.parameter.IndexCheckingTest.java

package com.yulikexuan.effectivejava.parameter;


import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test index checking methods of Objects - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class IndexCheckingTest {

    private List<String> names;
    private int length;

    @BeforeEach
    void setUp() {
        names = List.of("Index", "Checking", "Objects", "Java");
        length = this.names.size();
    }

    @Test
    void out_Of_Bounds_If_Index_Is_Invalid_With_CheckIndex() {

        // Given
        int index_1 = -1;
        int index_2 = this.length;

        // When & Then
        assertThatThrownBy(() -> Objects.checkIndex(index_1, this.length))
                .isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> Objects.checkIndex(index_2, this.length))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void out_Of_Bounds_If_Length_Is_Less_Than_Zero_With_CheckIndex() {

        // Given
        int index_1 = length - 1;

        // When & Then
        assertThatThrownBy(() -> Objects.checkIndex(index_1, 0))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void out_Of_Bounds_If_From_Index_Is_Invalid_With_CheckFromIndexSize() {

        // Given
        int fromIndex_1 = -1;
        int size_1 = 3;

        int fromIndex_2 = 2;
        int size_2 = 3;

        // When & Then
        assertThatThrownBy(() -> Objects.checkFromIndexSize(
                fromIndex_1, size_1, this.length))
                .isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> Objects.checkFromIndexSize(
                fromIndex_2, size_2, this.length))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void out_Of_Bounds_If_From_Index_Is_Greater_Than_To_Index() {

        // Given
        int fromIndex = 3;
        int toIndex = 2;

        // When & Then
        assertThatThrownBy(() -> Objects.checkFromToIndex(
                fromIndex, toIndex, this.length))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void out_Of_Bounds_If_To_Index_Is_Greater_Than_Length() {

        // Given
        int fromIndex = 1;
        int toIndex = this.length + 1;

        // When & Then
        assertThatThrownBy(() -> Objects.checkFromToIndex(
                fromIndex, toIndex, this.length))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

}///:~