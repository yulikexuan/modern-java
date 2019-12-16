//: com.yulikexuan.modernjava.collections.factories.ModernCollectionFactoryTest.java


package com.yulikexuan.modernjava.collections.factories;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


public class ModernCollectionFactoryTest implements ICollectionFactoriesTest {

    @BeforeEach
    void setUp() {
    }

    @Nested
    @DisplayName("Modern JDK - List Factory Test - ")
    class ModernJdkListFactoryTest {

        @DisplayName("List.of creates unmodifiable lists - ")
        @Test
        void test_List_Of_Factory() {

            // Given
            String[] nameArr = ICollectionFactoriesTest.getNames();

            /*
             * Allocates an extra array, which is wrapped up inside a list.
             * You pay the cost for allocating an array, initializing it, and
             * having it garbage-collected later
             */
            List<String> friendsFromArray = List.of(nameArr);

            /*
             * There is no extra array allocated for the list
             */
            List<String> friends = List.of("Raphael", "Olivia", "Thibaut");

            // When
            long difference = IntStream.range(0, friends.size())
                    .filter(i -> !friends.get(i).equals(nameArr[i]))
                    .count();

            // Then
            assertAll("List.of should create unmodifiable lists",
                    () -> assertThatThrownBy(() -> friends.add(NEW_NAME_1))
                            .as("Elements cannot be added")
                            .isInstanceOf(UnsupportedOperationException.class),
                    () -> assertThatThrownBy(() -> friends.remove(friends.size() - 1))
                            .as("Elements cannot be removed")
                            .isInstanceOf(UnsupportedOperationException.class),
                    () -> assertThatThrownBy(() -> friends.set(0, NEW_NAME_1))
                            .as("Elements cannot be replaced")
                            .isInstanceOf(UnsupportedOperationException.class),
                    () -> assertThatThrownBy(() -> List.of(NEW_NAME_1, null))
                            .as("Null elements are disallowed")
                            .isInstanceOf(NullPointerException.class));

            assertThat(difference).as(
                    "The order of elements in the list should be the " +
                            "same as the order of the provided arguments")
                    .isEqualTo(0L);
        }

    } //: End of class ModernJdkListFactoryTest

    @Nested
    @DisplayName("Modern JDK - Set Factory Test - ")
    class ModernJdkSetFactoryTest {

        @DisplayName("Set.of creates unmodifiable sets - ")
        @Test
        void test_Set_Of_Factory() {

            // Given
            Set<String> friends = Set.of("Raphael", "Olivia", "Thibaut");

            // When & Then
            assertAll("Set.of should create unmodifiable sets",
                    () -> assertThatThrownBy(() -> friends.add(NEW_NAME_1))
                            .as("Elements can not be added")
                            .isInstanceOf(UnsupportedOperationException.class),
                    () -> assertThatThrownBy(() -> friends.remove(NEW_NAME_1))
                            .as("Elements can not be removed")
                            .isInstanceOf(UnsupportedOperationException.class),
                    () -> assertThatThrownBy(() -> Set.of(NEW_NAME_1, null))
                            .as("Null elements are disallowed")
                            .isInstanceOf(NullPointerException.class));

            assertThatThrownBy(() -> Set.of("Raphael", "Raphael"))
                    .as("Duplicate elements passed to Set.of " +
                            "method result in IllegalArgumentException")
                    .isInstanceOf(IllegalArgumentException.class);
        }

    } //: End of class ModernJdkSetFactoryTest

    @Nested
    @DisplayName("Modern JDK - Map Factory Test - ")
    class ModernJdkMapFactoryTest {

        @DisplayName("Map.of creates unmodifiable maps - ")
        @Test
        void test_Map_Of_Factory() {

            // Given
            Map<String, Integer> ageOfFriends = Map.of("Raphael", 30,
                    "Olivia", 25, "Thibaut", 26);

            // Implemented with varargs and additional object-allocations are required
            Map<String, Integer> ageOfFriends_2 = Map.ofEntries(
                    entry("Raphael", 30),
                    entry("Olivia", 25),
                    entry("Thibaut", 26));

            // When

            // Then
            assertAll("Map.of should create unmodifiable maps",
                    () -> assertThatThrownBy(
                                    () -> ageOfFriends.put(NEW_NAME_1, 99))
                            .as("New entry cannot be added")
                            .isInstanceOf(UnsupportedOperationException.class),
                    () -> assertThatThrownBy(
                                    () -> ageOfFriends.remove(NEW_NAME_1))
                            .as("Entry cannot be removed")
                            .isInstanceOf(UnsupportedOperationException.class),
                    () -> assertThatThrownBy(
                                    () -> ageOfFriends.replace("Raphael", 32))
                            .as("Entry cannot be replaced")
                            .isInstanceOf(UnsupportedOperationException.class)
                    );

            assertAll("Map.of disallows null keys and null values",
                    () -> assertThatThrownBy(
                                    () -> Map.of(null, 12))
                            .as("Null key is not allow")
                            .isInstanceOf(NullPointerException.class),
                    () -> assertThatThrownBy(
                            () -> Map.of("Mike", null))
                            .as("Null value is not allow")
                            .isInstanceOf(NullPointerException.class));

            assertThatThrownBy(() -> Map.of("Raphael", 30,
                            "Raphael", 52))
                    .as("Duplicate elements passed to Map.of " +
                            "method result in IllegalArgumentException")
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}///:~