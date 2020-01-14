//: com.yulikexuan.modernjava.fp.LinkedListsTest.java


package com.yulikexuan.modernjava.fp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class LinkedListsTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test the basic linked list - ")
    void test_Creating_Linked_List() {

        // Given
        IList<Integer> list = LinkedList.builder()
                .head(5)
                .tail(LinkedList.builder()
                        .head(10)
                        .tail(EmptyList.newEmptyList()).build())
                .build();

        // When
        List<Integer> numbers = LinkedLists.getNumbers(list);

        // Then
        assertThat(numbers)
                .as("Should only contain 5 and 10")
                .containsExactly(5, 10);
    }

}///:~