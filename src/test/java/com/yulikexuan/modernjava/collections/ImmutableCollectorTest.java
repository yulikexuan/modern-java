//: com.yulikexuan.modernjava.collections.ImmutableCollectorTest.java


package com.yulikexuan.modernjava.collections;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ImmutableCollectorTest {

    public static final String EXPECTED_LIST_CLASS_NAME =
            "com.google.common.collect.RegularImmutableList";

    @Test
    @DisplayName("Test Immutable collectors of JDK - ")
    void test_Immutable_Collector_Funcs_Of_JDK() {

        // Given
        List<String> givenList = Arrays.asList("aaa", "bbb", "ccc");

        // When
        List<String> result = givenList.stream()
                .collect(collectingAndThen(toList(),
                        ImmutableList::copyOf));
        String listName = result.getClass().getName();

        // Then
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> result.remove("aaa"));
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> result.add("ddd"));
        assertThat(listName)
                .as("The list should be %1$s", EXPECTED_LIST_CLASS_NAME)
                .isEqualTo(EXPECTED_LIST_CLASS_NAME);
    }

    @Test
    @DisplayName("Test Guava's immutable collectors - ")
    void test_Guavas_Immutable_Collectors() {

        // Given
        List<Integer> numberList = IntStream.range(0, 10)
                .boxed()
                .collect(ImmutableList.toImmutableList());

        // When
        String actualListClassName = numberList.getClass().getName();

        // Then
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> numberList.remove(3));
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> numberList.add(12));
        assertThat(actualListClassName)
                .as("The actual list should be %1$s.",
                        EXPECTED_LIST_CLASS_NAME)
                .isEqualTo(EXPECTED_LIST_CLASS_NAME);
    }



}///:~