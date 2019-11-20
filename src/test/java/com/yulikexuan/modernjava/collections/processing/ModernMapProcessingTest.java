//: com.yulikexuan.modernjava.collections.processing.ModernMapProcessingTest.java


package com.yulikexuan.modernjava.collections.processing;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class ModernMapProcessingTest {

    private Map<String, Integer> ageOfFriends;
    private Map<String, String> favouriteMovies;

    @Mock
    BiConsumer<String, Integer> entryPrinter;

    @BeforeEach
    void setUp() {
        this.ageOfFriends = Map.of(
                "Raphael", 30,
                "Olivia", 25,
                "Thibaut", 26);
        this.favouriteMovies = Map.ofEntries(
                entry("Raphael", "Star Wars"),
                entry("Cristina", "Matrix"),
                entry("Olivia", "James Bond"));
    }

    @Nested
    @DisplayName("Modern Map Entry Accessing Test - ")
    class ModernMapEntryAccessingTest {

        private void printAges(String name, int age) {
            System.out.println(">>>>>>> - " + name + " is " +
                    age + " years old.");
        }

        private void verboseMapPrinter(Map<String, Integer> map) {
            for(Map.Entry<String, Integer> entry: map.entrySet()) {
                String friend = entry.getKey();
                Integer age = entry.getValue();
                entryPrinter.accept(friend, age);
            }
        }

        @DisplayName("Test modern forEach method of Map - ")
        @Test
        void test_Modern_Iteration_For_Each_Method_Of_Map() {

            // When
            verboseMapPrinter(ageOfFriends);
            ageOfFriends.forEach(entryPrinter);

            // Then
            then(entryPrinter).should(times(2))
                    .accept("Raphael", 30);
            then(entryPrinter).should(times(2))
                    .accept("Olivia", 25);
            then(entryPrinter).should(times(2))
                    .accept("Thibaut", 26);
        }

        @DisplayName("Test getOrDefault method of Map - ")
        @Test
        void test_Single_Entry_Accessing_With_Default_Value() {

            // When
            String movieName_1 = favouriteMovies.getOrDefault("Raphael",
                    "Midway");
            String movieName_2 = favouriteMovies.getOrDefault("Yul",
                    "Midway");

            // Then
            assertThat(movieName_1)
                    .as("The movie name for Raphael should be 'Star Wars'")
                    .isEqualTo("Star Wars");
            assertThat(movieName_2)
                    .as("The movie name for Yul should be 'Midway'")
                    .isEqualTo("Midway");
        }

        private String getDefaultPhone() {
            return "000-000-0000";
        }

        @Test
        void test_GetOrDefault_For_Null_Entry_Value_() {

            // Given
            Map<String, String> friendPhones = new HashMap<>();
            friendPhones.put("Rahpael", "123-456-7890");
            friendPhones.put("Olivia", null);

            // When
            String phone_1_0 = friendPhones.getOrDefault("Rahpael",
                    "987-654-3210");
            String phone_1_1 = friendPhones.getOrDefault("Rahpael",
                    getDefaultPhone());

            String phone_2_0 = friendPhones.getOrDefault("Olivia",
                    "111-222-333-4444");
            String phone_2_1 = friendPhones.getOrDefault("Olivia",
                    getDefaultPhone());

            // Then
            assertThat(phone_1_0).isEqualTo("123-456-7890");
            assertThat(phone_1_1).isEqualTo("123-456-7890");
            assertThat(phone_2_0).isNull();
            assertThat(phone_2_1).isNull();
        }

    }//: End of class ModernMapEntryAccessingTest

    @Nested
    @DisplayName("Modern Map Sorting Test - ")
    class ModernMapSortingTest {

        Collector<Map.Entry<String, String>, ?, Map<String, String>>
                sortedMapCollector;

        @BeforeEach
        void setUp() {

            this.sortedMapCollector = toMap(
                    entry -> entry.getKey(),
                    entry -> entry.getValue(),
                    /*
                     * mergeFunction, used to resolve collisions between values
                     * associated with the same key
                     */
                    (e1, e2) -> e1,
                    LinkedHashMap::new);
        }

        @DisplayName("Test map sorting by entry-value - ")
        @Test
        void test_Sorting_Map_Entries_By_Value() {

            // When
            Map<String, String> sortedMoviesByMoviename =
                    favouriteMovies.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .collect(sortedMapCollector);

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
                    favouriteMovies.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .collect(sortedMapCollector);

            // Then
            assertThat(sortedMoviesByMoviename)
                    .as("Entries should be sorted by friend's name")
                    .containsExactly(
                            entry("Cristina", "Matrix"),
                            entry("Olivia", "James Bond"),
                            entry("Raphael", "Star Wars"));
        }

    } //: End of class ModernMapSortingTest

    @Nested
    @DisplayName("Modern Map Compute Patterns Test - ")
    class ModernMapComputePatternsTest {

        private MessageDigest messageDigest;

        private int digestTimes;

        @BeforeEach
        void setUp() throws NoSuchAlgorithmException {
            messageDigest = MessageDigest.getInstance("SHA-256");
            digestTimes = 0;
        }

        private byte[] calculateDigest(String key) {
            this.digestTimes++;
            return this.messageDigest.digest(key.getBytes(StandardCharsets.UTF_8));
        }

        @DisplayName("Test Modern Map's computeIfAbsent method for Singl Value - ")
        @Test
        void test_Modern_Map_computeIfAbsent_For_Single_Value() {

            // Given: Only two lines are calculated
            Map<String, byte[]> dataToHash = new HashMap<>();

            List<String> lines = List.of(
                    RandomStringUtils.randomAlphanumeric(10),
                    RandomStringUtils.randomAlphanumeric(10),
                    RandomStringUtils.randomAlphanumeric(10),
                    RandomStringUtils.randomAlphanumeric(10),
                    RandomStringUtils.randomAlphanumeric(10));

            dataToHash.put(lines.get(0), this.messageDigest.digest(
                    lines.get(0).getBytes(StandardCharsets.UTF_8)));
            dataToHash.put(lines.get(1), this.messageDigest.digest(
                    lines.get(1).getBytes(StandardCharsets.UTF_8)));

            // When
            lines.forEach(line -> dataToHash.computeIfAbsent(line,
                    this::calculateDigest));

            // Then
            assertThat(this.digestTimes)
                    .as("Should only calculate three times")
                    .isEqualTo(3);
        }

        @DisplayName("Test Modern Map's computeIfAbsent method for Multiple Values - ")
        @Test
        void test_Modern_Map_computeIfAbsent_For_Multi_Values() {

            // Given
            String[] names = {"Raphael", "Olivia", "Thibaut"};
            Map<String, List<String>> friendMovies = new HashMap<>();
            Arrays.stream(names).forEach(name -> friendMovies.put(name, null));

            // When
            Arrays.stream(names).forEach(name -> friendMovies.computeIfAbsent(
                    name, n -> new ArrayList<>()).add("Midway"));

            // Then
            System.out.println(friendMovies);
        }

    }//: End of class ModernMapComputePatternsTest

}///:~