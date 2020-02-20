//: com.yulikexuan.modernjava.collections.MiscellaneousLibraryUpdateTest.java


package com.yulikexuan.modernjava.collections;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
 *
 */
public class MiscellaneousLibraryUpdateTest {

    /*
     * getOrDefault, forEach, compute, computeIfAbsent, computeIfPresent, merge,
     * putIfAbsent, remove(key, value), replace, replaceAll, of, ofEntries
     */
    @Nested
    @DisplayName("New Map Methods Test - ")
    class MiscellaneousMapUpdateTest {

        private String name = "Aston Martin";

        @Test
        @DisplayName("Test value for unexisting key without getOrDefault - ")
        void test_Get_Value_Of_A_Key_Or_Return_Default_Mannually() {

            // Given
            Map<String, Integer> inventory = Maps.newHashMap();

            // The default value
            Integer count = 0;

            // When
            if (inventory.containsKey(this.name)) {
                count = inventory.get(this.name);
            }

            // Then
            assertThat(count).as("Should be the default value: 0")
                    .isEqualTo(0);
        }

        /*
         * Works only if there’s no mapping
         */
        @Test
        @DisplayName("Test value for unexisting key with getOrDefault - ")
        void test_getOrDefault_Method_Of_Map() {

            // Given
            Map<String, Integer> inventory = Maps.newHashMap();

            // The default value
            int count = 0;

            // When

            // The inventory for the name
            int actualCount = inventory.getOrDefault(name, count);

            // Then
            assertThat(actualCount)
                    .as("Should be the default value: 0")
                    .isEqualTo(count);
        }

        @Test
        void test_computeIfAbsent_Method() {

        }

    }//: End of class MiscellaneousMapUpdateTest

    @Nested
    @DisplayName("New Collection Methods Test - ")
    class MiscellaneousCollectionUpdateTest {

        @Test
        @DisplayName("Test removeIf method for List - ")
        void test_removeIf_Method_Of_List() {

            // Given
            List<String> logContent = Lists.newArrayList(
                    "WARNING-" + UUID.randomUUID().toString(),
                    "WARNING-" + UUID.randomUUID().toString(),
                    "ERROR-" + UUID.randomUUID().toString(),
                    "INFO-" + UUID.randomUUID().toString(),
                    "WARNING-" + UUID.randomUUID().toString());

            // When
            boolean removed = logContent.removeIf(log -> log.startsWith("WARNING-"));

            // Then
            assertThat(logContent).size().isEqualTo(2);
            assertThat(logContent).doesNotContainSubsequence("WARNING-");
        }

        @Test
        @DisplayName("Test removeIf method for Set - ")
        void test_removeIf_Method_Of_Set() {

            // Given
            Set<String> logContent = Sets.newHashSet(
                    "WARNING-" + UUID.randomUUID().toString(),
                    "WARNING-" + UUID.randomUUID().toString(),
                    "ERROR-" + UUID.randomUUID().toString(),
                    "INFO-" + UUID.randomUUID().toString(),
                    "WARNING-" + UUID.randomUUID().toString());

            // When
            boolean removed = logContent.removeIf(log -> log.startsWith("WARNING-"));

            // Then
            assertThat(logContent).size().isEqualTo(2);
            assertThat(logContent).doesNotContainSubsequence("WARNING-");
        }

    }//: End of class MiscellaneousCollectionUpdateTest

    @Nested
    @DisplayName("New List Methods Test - ")
    class MiscellaneousListUpdateTest {

        @Test
        @DisplayName("Test replaceAll method of List - ")
        void test_ReplaceAll_Method() {

            // Given
            List<Integer> numbers = Lists.newArrayList(1, 2, 3, 4, 5);

            // When
            numbers.replaceAll(number -> number * 2);

            // Then
            assertThat(numbers).containsSequence(2, 4, 6, 8, 10);
        }

    }//: End of class MiscellaneousListUpdateTest

    @Nested
    @DisplayName("Collections Methods Test - ")
    class MiscellaneousCollectionsTest {

        @Test
        void test_If_Two_Collections_Having_No_Elements_In_Common() {

            // Given
            Collection<Integer> collection1 = IntStream.range(0, 10).boxed()
                    .collect(ImmutableList.toImmutableList());
            Collection<Integer> collection2 = IntStream.range(6, 15).boxed()
                    .collect(ImmutableList.toImmutableList());
            Collection<Long> collection3 = LongStream.range(10, 20).boxed()
                    .collect(ImmutableList.toImmutableList());

            // When
            boolean noElementsInCommon_1_2 = Collections.disjoint(collection1,
                    collection2);
            boolean noElementsInCommon_2_3 = Collections.disjoint(collection2,
                    collection3);

            // Then
            assertThat(noElementsInCommon_1_2).isFalse();
            assertThat(noElementsInCommon_2_3).isTrue();
        }

        @Test
        void test_Empty_List_From_Collections_Is_Immutable() {

            // Given
            List<String> emptyStringList = Collections.emptyList();

            // When
            assertThatThrownBy(() -> emptyStringList.add("YUL"))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        void test_Rotating() {

            // Given
            List<Integer> numbers = IntStream.range(0, 10).boxed()
                    .collect(Collectors.toList());

            // When
            Collections.rotate(numbers, 2);

            // Then
            assertThat(numbers).containsSequence(8, 9, 0, 1, 2, 3);
        }

        @Test
        void test_Singleton() {

            // Given
            int specifiedElem = 7;

            // When
            Set<Integer> numSet = Collections.singleton(specifiedElem);
            List<Integer> numList = Collections.singletonList(specifiedElem);
            Map<String, Integer> numMap = Collections.singletonMap(
                    "YUL", specifiedElem);
            // Then
            assertThat(numSet).containsExactly(specifiedElem);
            assertThat(numList).containsExactly(specifiedElem);
            assertThat(numMap).containsExactly(MapEntry.entry("YUL", 7));
        }

    }//: End of class MiscellaneousCollections

}///:~