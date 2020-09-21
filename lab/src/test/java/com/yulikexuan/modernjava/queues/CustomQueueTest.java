//: com.yulikexuan.modernjava.queues.CustomQueueTest.java


package com.yulikexuan.modernjava.queues;


import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


class CustomQueueTest {

    private int size;
    private String[] names;
    private Queue<String> customQueue;

    @BeforeEach
    void setUp() {
        this.size = 3;
        this.names = new String[] {
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10)
        };
        this.customQueue = CustomQueue.of();
    }

    @Test
    void test_Basic_Methods_Of_CustomQueue() {

        // Given
        IntStream.range(0, size)
                .mapToObj(i -> this.names[i])
                .forEach(s -> this.customQueue.add(s));

        // When
        List<String> actualNameList = IntStream.range(0, size)
                .mapToObj(i -> this.customQueue.poll())
                .collect(ImmutableList.toImmutableList());

        // Then
        assertThat(actualNameList).containsExactly(this.names);
    }

}///:~