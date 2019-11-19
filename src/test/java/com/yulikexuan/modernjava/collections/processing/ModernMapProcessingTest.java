//: com.yulikexuan.modernjava.collections.processing.ModernMapProcessingTest.java


package com.yulikexuan.modernjava.collections.processing;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collector;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;


public class ModernMapProcessingTest {

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

    @DisplayName("Test modern forEach method of Map - ")
    @Test
    void test_Modern_Iteration_For_Each_Method_Of_Map() {

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

    @Nested
    @DisplayName("Modern Map Sorting Test - ")
    class ModernSortingTest {

        private Map<String, String> favouriteMovies;

        Collector<Map.Entry<String, String>, ?, Map<String, String>>
                linkedHashMapCollector = toMap(
                entry -> entry.getKey(),
                entry -> entry.getValue(),
                /*
                 * mergeFunction, used to resolve collisions between values
                 * associated with the same key
                 */
                (e1, e2) -> e1,
                LinkedHashMap::new);

        @BeforeEach
        void setUp() {
            this.favouriteMovies = Map.ofEntries(
                    entry("Raphael", "Star Wars"),
                    entry("Cristina", "Matrix"),
                    entry("Olivia", "James Bond"));
        }

        @DisplayName("Test map sorting by entry-value - ")
        @Test
        void test_Sorting_Map_Entries_By_Value() {

            // When
            Map<String, String> sortedMoviesByMoviename =
                    this.favouriteMovies.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .collect(linkedHashMapCollector);

            // Then
            assertThat(sortedMoviesByMoviename)
                    .as("Entries should be sorted by movie's name")
                    .containsExactly(
                            entry("Olivia", "James Bond"),
                            entry("Cristina", "Matrix"),
                            entry("Raphael", "Star Wars"));
        }

        @DisplayName("Test map sorting by entry-key - ")
        @Test
        void test_Sorting_Map_Entries_By_Key() {

            // When
            Map<String, String> sortedMoviesByMoviename =
                    this.favouriteMovies.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .collect(linkedHashMapCollector);

            // Then
            assertThat(sortedMoviesByMoviename)
                    .as("Entries should be sorted by friend's name")
                    .containsExactly(
                            entry("Cristina", "Matrix"),
                            entry("Olivia", "James Bond"),
                            entry("Raphael", "Star Wars"));
        }

    } //: End of class ModernSortingTest

}///:~