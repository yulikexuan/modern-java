//: com.yulikexuan.modernjava.collections.processing.ModernMapProcessingTest.java


package com.yulikexuan.modernjava.collections.processing;


import com.google.common.collect.Maps;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class ModernMapProcessingTest {

    private Map<String, Integer> ageOfFriends;
    private Map<String, String> favouriteMovies;

    @Mock
    private BiConsumer<String, Integer> entryPrinter;

    private String[] friendNames;
    private Map<String, List<String>> friendMovies;

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

        friendNames = new String[] {"Raphael", "Olivia", "Thibaut"};

        friendMovies = new HashMap<>();
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

// The implementation of computeIfAbsent:
//
//            if (map.get(key) == null) {
//                V newValue = mappingFunction.apply(key);
//                if (newValue != null)
//                    map.put(key, newValue);
//            }

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
            Arrays.stream(friendNames).forEach(name -> friendMovies.put(name, null));
            friendMovies.put("yul", new ArrayList<>());
            friendMovies.get("yul").add("Midway");

            // When
            friendMovies.computeIfAbsent("Raphael", n -> new ArrayList<>())
                    .add("Star Wars");
            friendMovies.computeIfAbsent("Olivia", n -> new ArrayList<>())
                    .add("James Bond");
            friendMovies.computeIfAbsent("Thibaut", n -> new ArrayList<>())
                    .add("Matrix");
            friendMovies.computeIfAbsent("yul", n -> new ArrayList<>())
                    .add("Matrix");
            friendMovies.computeIfAbsent("Cristina", n -> new ArrayList<>())
                    .add("The Ring");

            // Then
            assertAll("Each friend has their own movies",
                    () -> assertThat(friendMovies.get("Raphael"))
                            .containsExactly("Star Wars"), // Before: value was null
                    () -> assertThat(friendMovies.get("Olivia"))
                            .containsExactly("James Bond"), // Before: value was null
                    () -> assertThat(friendMovies.get("Thibaut"))
                            .containsExactly("Matrix"), // Before: value was null
                    () -> assertThat(friendMovies.get("yul"))
                            .containsExactly("Midway", "Matrix"), // Before has one value
                    () -> assertThat(friendMovies.get("Cristina"))
                            .containsExactly("The Ring")); // Before has no key in map
        }

        @DisplayName("Test Map::computeIfPresent method - ")
        @Test
        void test_Modern_Map_computeIfPresent_Method() {

// The implementation of computeIfPresent:
//
//            if (map.get(key) != null) {
//                V oldValue = map.get(key);
//                V newValue = remappingFunction.apply(key, oldValue);
//                if (newValue != null)
//                    map.put(key, newValue);
//                else
//                    map.remove(key);
//            }

            // Given
            friendMovies.computeIfAbsent("Raphael",
                    n -> new ArrayList<>()).add("Star Wars");

            friendMovies.put("Olivia", null);

            friendMovies.computeIfAbsent("Cristina",
                    k -> new ArrayList<>()).add("Matrix");

            // When
            friendMovies.computeIfPresent("Raphael",
                    (k, v) -> { v.add("Midway 2019"); return v;});
            friendMovies.computeIfPresent("Olivia",
                    (k, v) -> {v.add("007"); return v;});
            friendMovies.computeIfPresent("Cristina", (k, v) -> null);

            // Then
            assertThat(friendMovies.get("Raphael"))
                    .containsExactly("Star Wars", "Midway 2019");
            assertThat(friendMovies).containsKey("Olivia");
            assertThat(friendMovies.get("Olivia")).isNull();
            assertThat(friendMovies).doesNotContainKey("Cristina");
        }

        @DisplayName("Test Map::compute method - ")
        @Test
        void test_Map_compute_Method() {

// The implementation of compute:
//
//            V oldValue = map.get(key);
//            V newValue = remappingFunction.apply(key, oldValue);
//            if (oldValue != null) {
//                if (newValue != null)
//                    map.put(key, newValue);
//                else
//                    map.remove(key);
//            } else {
//                if (newValue != null)
//                    map.put(key, newValue);
//                else
//                    return null;
//            }

            // Given
            friendMovies.computeIfAbsent("Raphael",
                    k -> new ArrayList<>()).add("Star Wars");
            friendMovies.computeIfAbsent("Olivia",
                    k -> new ArrayList<>()).add("007");
            friendMovies.put("yul", null);

            // When
            friendMovies.compute("Raphael",
                    (k, v) -> { v.add("Midway 2019"); return v;});
            friendMovies.compute("Olivia", (k, v) -> null);
            friendMovies.compute("yul", (k, v) -> {
                return List.of();
            });

            List<String> moviesForCristina = friendMovies.compute(
                    "Cristina", (k, v) -> null);

            // Then
            assertThat(friendMovies.get("Raphael"))
                    .containsExactly("Star Wars", "Midway 2019");
            assertThat(friendMovies).doesNotContainKey("Olivia");
            assertThat(friendMovies.get("yul")).isNotNull();
            assertThat(friendMovies).doesNotContainKey("Cristina");
            assertThat(moviesForCristina).isNull();
        }

    }//: End of class ModernMapComputePatternsTest

    @DisplayName("Modern Map Remove Patterns Test - ")
    @Nested
    class ModernMapRemovePatternsTest {

        @DisplayName("Test Map::remove(k, v) method - ")
        @Test
        void test_Removing_The_Entry_Only_If_Having_The_Specified_Value_Currently() {

//            How to Do it Before Java 8:

//            String key = "Raphael";
//            String value = "Jack Reacher 2";
//            if (favouriteMovies.containsKey(key) &&
//                    Objects.equals(favouriteMovies.get(key), value)) {
//                favouriteMovies.remove(key);
//                return true;
//            }
//            else {
//                return false;
//            }
//
//            The implementation of remove:
//            if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
//                map.remove(key);
//                return true;
//            } else
//                return false;

            // Given
            favouriteMovies = new HashMap<>();
            favouriteMovies.computeIfAbsent("Raphael", k -> "Jack Reacher 2");

            // When
            boolean removed = favouriteMovies.remove("Raphael", "Jack Reacher 2");

            // Then
            assertAll("The entry of key Raphael should have been removed",
                    () -> assertThat(favouriteMovies)
                            .as("Key 'Raphael' should already been removed")
                            .doesNotContainKey("Raphael"),
                    () -> assertThat(removed).isTrue());
        }

        @Test
        @DisplayName("Test removeIf from Map - ")
        void test_Modern_Map_removeIf_Method() {

            // Given
            Map<String, Integer> movieBoxOffice = Maps.newHashMap();

            movieBoxOffice.put("Star Wars", 1234);
            movieBoxOffice.put("Matrix", 2134);
            movieBoxOffice.put("James Bond", 3412);
            movieBoxOffice.put("Midway", 4321);

            // When
            boolean removed = movieBoxOffice.entrySet().removeIf(
                    e -> e.getValue() < 2000);

            // Then
            assertThat(movieBoxOffice).doesNotContainKey("Star Wars");
            assertThat(removed).isTrue();
        }

    } //: End of class ModernMapRemovePatternsTest

    @Nested
    @DisplayName("Modern Map")
    class ModernMapReplacementPatternsTest {

        @BeforeEach
        void setUp() {
            favouriteMovies = new HashMap<>() { {
                put("Raphael", "Star Wars");
                put("Cristina", "Matrix");
                put("Olivia", "James Bond");
            } };
        }

        @Test
        void test_Map_Replace_All_Method() {

//            The implementation of replaceAll:
//            for (Map.Entry<K, V> entry : map.entrySet())
//                entry.setValue(function.apply(entry.getKey(), entry.getValue()));

            // When
            favouriteMovies.replaceAll((k, v) -> v.toUpperCase());
            Collection<String> names = favouriteMovies.values();

            // Then
            names.stream().forEach(name -> assertThat(name).isUpperCase());
        }

        @Test
        void test_Map_Replace_Method() {

//            The implementation of replace:
//            if (map.containsKey(key)) {
//                return map.put(key, value);
//            } else {
//                return null;
//            }

            // When
            String thePreviousValue = favouriteMovies.replace("Raphael", "Midway");

            // Then
            assertThat(favouriteMovies).containsEntry("Raphael", "Midway");
            assertThat(thePreviousValue).isEqualTo("Star Wars");
        }

        @Test
        void test_Map_Conditional_Replace_Methods() {

//            The implementation of conditional replace:
//            if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
//                map.put(key, newValue);
//                return true;
//            } else {
//                return false;
//            }

            // When
            boolean replaced = favouriteMovies.replace(
                    "Raphael", "Star Wars", "Midway");
            boolean replaced2 = favouriteMovies.replace(
                    "Olivia", "the Simpsons", "007");

            // Then
            assertThat(favouriteMovies).containsEntry("Raphael", "Midway");
            assertThat(favouriteMovies).doesNotContainValue("007");
            assertThat(replaced).isTrue();
            assertThat(replaced2).isFalse();
        }

    } //: End of class ModernMapReplacementPatternsTest

    @Nested
    @DisplayName("Modern Map::merge Test - ")
    class ModernMapMergeTest {

        private Map<String, String> family;
        private Map<String, String> friends;
        private Map<String, String> everyone;

        @BeforeEach
        void setUp() {

//            Implementation of Map::merge(K key, V value,
//                    BiFunction<? super V,​? super V,​? extends V> remappingFunction)
//
//            key should not be null
//            value should not be null
//            remappingFunction should not be null
//
//            V oldValue = map.get(key);
//            V newValue = (oldValue == null) ? value :
//                    remappingFunction.apply(oldValue, value);
//            if (newValue == null)
//                map.remove(key);
//            else
//                map.put(key, newValue);
//

            family = Map.ofEntries(
                    entry("Teo", "Star Wars"),
                    entry("Cristina", "James Bond"));

            friends = Map.ofEntries(
                    entry("Raphael", "Star Wars"),
                    entry("Cristina", "Matrix"));
        }

        @Test
        @DisplayName("Test Using Map::putAll for Merging - ")
        void test_Map_Verbose_Merge_Solution() {

            // Given
            everyone = new HashMap<>(family);

            // When
            everyone.putAll(friends);

            // Then
            assertAll("Map::putAll method cannot merge two map " +
                    "into one correctly with duplicated keys",
                    () -> assertThat(everyone).containsEntry("Teo", "Star Wars"),
                    () -> assertThat(everyone).containsEntry("Raphael", "Star Wars"),
                    () -> assertThat(everyone).containsEntry("Cristina", "Matrix"),
                    () -> assertThat(everyone).doesNotContainEntry("Cristina", "James Bond"));
        }

        private BiConsumer<String, String> movieMerge =
                (k, v) -> everyone.merge(k, v, (v1, v2) -> v1 + ", " + v2);

        @Test
        @DisplayName("Test Map::merge with Duplicated Key - ")
        void test_Modern_Map_merge_Method_With_Duplicated_Key() {

            // Given
            everyone = new HashMap<>(family);

            // When
            friends.forEach(movieMerge);

            // Then
            assertAll("Map::merge method can merge two map " +
                            "into one correctly even with duplicated keys",
                    () -> assertThat(everyone)
                            .containsEntry("Teo", "Star Wars"),
                    () -> assertThat(everyone)
                            .containsEntry("Raphael", "Star Wars"),
                    () -> assertThat(everyone)
                            .containsEntry("Cristina", "James Bond, Matrix"));
        }

        @Test
        @DisplayName("Test Map::merge with Unassociated Key - ")
        void test_Modern_Map_merge_Method_With_Unassociated_Key() {

            // Given
            everyone = new HashMap<>(family);
            Map<String, String> kids = Map.ofEntries(
                    entry("Jerry", "the Simpsons"));

            // When
            kids.forEach(movieMerge);

            // Then
            assertAll("Map::merge method can merge two map " +
                            "into one correctly even with unassociated key",
                    () -> assertThat(everyone)
                            .containsEntry("Teo", "Star Wars"),
                    () -> assertThat(everyone)
                            .containsEntry("Cristina", "James Bond"),
                    () -> assertThat(everyone)
                            .containsEntry("Jerry", "the Simpsons"));
        }

        @Test
        @DisplayName("Test Map::merge with null Value - ")
        void test_Modern_Map_merge_Method_With_Null_Value() {

            // Given
            everyone = new HashMap<>(family);
            everyone.put("Jerry", null);
            Map<String, String> kids = Map.ofEntries(
                    entry("Jerry", "the Simpsons"));

            // When
            kids.forEach(movieMerge);

            // Then
            assertAll("Map::merge method can merge two map " +
                            "into one correctly even with unassociated key",
                    () -> assertThat(everyone)
                            .containsEntry("Teo", "Star Wars"),
                    () -> assertThat(everyone)
                            .containsEntry("Cristina", "James Bond"),
                    () -> assertThat(everyone)
                            .containsEntry("Jerry", "the Simpsons"),
                    () -> assertThat(everyone.size()).isEqualTo(3));
        }

        @Test
        @DisplayName("Test Map::merge with null Value - ")
        void test_Modern_Map_merge_Method_With_Null_New_Value() {

            // Given
            everyone = new HashMap<>(family);

            // When
            // (k, v) -> everyone.merge(k, v, (v1, v2) -> v1 + ", " + v2);
            everyone.merge("Cristina", "Matrix", (v1, v2) -> null);

            // Then
            assertThat(everyone).doesNotContainKey("Cristina");
        }

        @Test
        @DisplayName("Test how Map::merge method is used to check Initialization - ")
        void test_Modern_Map_merge_For_Checking_Initialization() {

            // Given
            String[] movies = {"Star Wars", "James Bond", "Matrix", "Midway"};
            Map<String, Integer> moviesToCount =  Maps.newHashMap();
            moviesToCount.put(movies[0], null);
            moviesToCount.put(movies[1], 1);

            // When
            Arrays.stream(movies).forEach(
                    movie -> moviesToCount.merge(movie, 1, (k, v) -> ++v));

            // Then
            assertThat(moviesToCount).as("Each movie should " +
                            "have been watched at least once")
                    .contains(
                            entry(movies[0], 1),
                            entry(movies[1], 2),
                            entry(movies[2], 1),
                            entry(movies[3], 1));
        }

    } //: End of class ModernMapMergeTest

}///:~