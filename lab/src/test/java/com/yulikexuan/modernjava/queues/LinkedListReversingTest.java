//: com.yulikexuan.modernjava.queues.LinkedListReversingTest.java


package com.yulikexuan.modernjava.queues;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkedListReversingTest {

    private Integer[] numbers;
    private Integer[] reversedNumbers;

    private LinkedList<Integer> linkedNumberList;
    private LinkedList<Integer> reversedLinkedNumberList;

    @BeforeEach
    void setUp() {
        this.numbers = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7};
        this.reversedNumbers = new Integer[] {7, 6, 5, 4, 3, 2, 1, 0};

        this.linkedNumberList = new LinkedList(Arrays.asList(this.numbers));
    }

    @Test
    void test_Reversing_Linked_Number_List() {

        // Given
        int size = this.linkedNumberList.size();
        Integer head = this.linkedNumberList.peek();
        Integer tail = this.linkedNumberList.peekLast();

        // When
        for (int i = 0; i < size - 1; i++) {
            this.linkedNumberList.offerLast(this.linkedNumberList.pop());
        }

        // Then
        assertThat(this.linkedNumberList).containsExactly(this.reversedNumbers);

    }

}///:~