//: com.yulikexuan.modernjava.collections.processing.ModernCollectionProcessingTest.java


package com.yulikexuan.modernjava.collections.processing;


import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Modern Collection Processing Methods Test - ")
public class ModernCollectionProcessingTest {

    private String[] names = {"Raphael", "Olivia", "Thibaut"};

    private List<String> friendNames;
    private String[] lowerCaseNames;

    @BeforeEach
    void setUp() {
        friendNames = Arrays.stream(names).collect(Collectors.toList());
        lowerCaseNames = new String[]{"raphael", "olivia", "thibaut"};
    }

    @Nested
    @DisplayName("Modern List Processing Methods Test - ")
    class ModernListProcessiongTest {

        @DisplayName("Test error prone processing about collection - ")
        @Test
        void test_Error_Prone_Collection_Processing() {

            // Given

            // When & Then
            assertThatThrownBy(() -> {
                for (String name: friendNames) {
                    if (name.charAt(0) == 'R') {
                        /*
                         * The state of the iterator is no longer synced with the
                         * state of the collection, and vice versa.
                         */
                        friendNames.remove(name);
                    }
                }})
                    .as("Should throw ConcurrentModificationException")
                    .isInstanceOf(ConcurrentModificationException.class);
        }

        @DisplayName("Test 'removeif' method of Collection - ")
        @Test
        void test_Conditional_Removing() {

            // When
            boolean removed = friendNames.removeIf(name -> name.charAt(0) == 'R');

            // Then
            assertThat(removed).as("'Raphael' should be removed")
                    .isTrue();
        }

        private String toLowerCase(String name) {
            return Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }

        @DisplayName("Show how verbose are the replace processing - ")
        @Test
        void test_Verbose_Replace_All_Method_Of_List() {

            // When
            List<String> lowerCaseNameList = friendNames.stream()
                    .map(this::toLowerCase)
                    .collect(ImmutableList.toImmutableList());

            for (ListIterator<String> its = friendNames.listIterator();
                 its.hasNext(); ) {
                String name = its.next();
                its.set(this.toLowerCase(name));
            }

            // Then
            assertThat(lowerCaseNameList).containsExactly(lowerCaseNames);
            assertThat(friendNames).containsExactly(lowerCaseNames);
        }

        @DisplayName("Test the modern 'replaceAll' method - ")
        @Test
        void test_Modern_Replace_All_Method_Of_List() {

            // When
            friendNames.replaceAll(this::toLowerCase);

            // Then
            assertThat(friendNames)
                    .as("All names should be lower case")
                    .containsExactly(lowerCaseNames);
        }

    } //: End of class ModernListProcessiongTest

    @Nested
    @DisplayName("Modern Map Processing Methods Test - ")
    class ModernMapProcessiongTest {

        private Map<String, Integer> ageOfFriends;

        @BeforeEach
        void setUp() {
            ageOfFriends = Map.of(
                    "Raphael", 30,
                    "Olivia", 25,
                    "Thibaut", 26);
        }

        private void printAges(String name, int age) {
            System.out.println(">>>>>>> - " + name + " is " +
                    age + " years old.");
        }

        @DisplayName("Test modern iteration method of Map - ")
        @Test
        void test_Modern_Iteration_Method_Of_Map() {

            // Given
            System.out.println("------- [Verbose printing]: ");
            for(Map.Entry<String, Integer> entry: ageOfFriends.entrySet()) {
                String friend = entry.getKey();
                Integer age = entry.getValue();
                this.printAges(friend, age);
            }

            // When
            System.out.println("+++++++ [Modern printing]: ");
            ageOfFriends.forEach(this::printAges);
        }

    }

}///:~