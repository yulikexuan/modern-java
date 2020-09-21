//: com.yulikexuan.modernjava.algorithms.StackQueueTest.java


package com.yulikexuan.modernjava.algorithms;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


class StackQueueTest {

    private static final int SIZE = 8;

    private Queue<Integer> stackQueue;

    private List<Integer> result;

    @BeforeEach
    void setUp() {
        this.stackQueue = new StackQueue<>();
        this.result = new ArrayList<>();
    }


    @Test
    void test_Stack_Queue_Operations() {

        // Given
        IntStream.range(0, 8)
                .forEach(i -> this.stackQueue.offer(i));

        // When
        while (!this.stackQueue.isEmpty()) {
            this.result.add(this.stackQueue.poll());
        }

        // Then
        assertThat(this.result).containsExactly(0, 1, 2, 3, 4, 5, 6, 7);
    }

}///:~