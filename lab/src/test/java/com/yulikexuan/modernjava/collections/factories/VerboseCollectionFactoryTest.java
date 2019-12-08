//: com.yulikexuan.modernjava.collections.factories.VerboseCollectionFactoryTest.java


package com.yulikexuan.modernjava.collections.factories;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Index.atIndex;
import static org.junit.jupiter.api.Assertions.assertAll;


public class VerboseCollectionFactoryTest {

    private List<String> friends;
    private String newName_1;

    @BeforeEach
    void setUp() {
        this.friends = Arrays.asList("Raphael", "Olivia");
        this.newName_1 = "Richard";
    }

    @DisplayName("Test List Factories - ")
    @Nested
    class VerboseListFactoriesTest {

        @DisplayName("List built on array can be updated - ")
        @Test
        void test_Arrays_As_List_Can_Be_Updated() {

            // Given

            // When
            friends.set(0, newName_1);

            // Then
            assertThat(friends)
                    .as("The first element of the list built on array " +
                            "should be updated.")
                    .contains(newName_1, atIndex(0));
        }

        @DisplayName("The size of a List built on array can not be changed - ")
        @Test
        void test_The_Size_Of_Arrays_As_List_Can_Not_Be_Changed() {
            // When
            assertAll("The size of the List built on Arrays.asList should " +
                            "not be able to changed.",
                    () -> assertThatThrownBy(() -> friends.add(newName_1))
                            .as("Should throw UnsupportedOperationException " +
                                    "if adding new element to a List built on Array")
                            .isInstanceOf(UnsupportedOperationException.class),
                    () -> assertThatThrownBy(() -> friends.remove(friends.size() - 1))
                            .as("Should throw UnsupportedOperationException if " +
                                    "removing any existing element from a List built on Array")
                            .isInstanceOf(UnsupportedOperationException.class));
        }

    }//: End of class VerboseListFactoriesTest

    @DisplayName("Test Set Factories - ")
    @Nested
    class VerboseSetFactoriesTest {

        @DisplayName("Set built on List is mutable - ")
        @Test
        void test_Set_Built_On_Arrays_As_List() {
            // When
            Set<String> friendSet = new HashSet<>(friends);
            friendSet.add(newName_1);

            // Then
            assertThat(friendSet).as("Set from List should be mutable.")
                    .contains(newName_1);
        }

        @DisplayName("Set built by Stream is mutable - ")
        @Test
        void test_Set_Built_By_Stream() {

            // Given
            Set<String> friendSet = Stream.of("Raphael", "Olivia")
                    .collect(Collectors.toSet());

            // When
            friendSet.add(newName_1);
            friendSet.remove("Raphael");

            // Then
            assertAll("Set built by Stream should be mutable.",
                    () -> assertThat(friendSet)
                            .as("Set should contain '%1$s'.", newName_1)
                            .contains(newName_1),
                    () -> assertThat(friendSet)
                            .as("Set should not contain 'Raphael'.")
                            .doesNotContain("Raphael"));
        }
    }

}///:~