//: com.yulikexuan.modernjava.immutability.ImmutableCollectionsTest.java


package com.yulikexuan.modernjava.immutability;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Immutable Collections Test - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ImmutableCollectionsTest {

    private static final String[] NAME_LIST = {
            "John", "Buddy", "Vishwa", "Amy",
    };

    @Nested
    @DisplayName("Immutable List Test - ")
    class ImmutableListTest {

        private List<String> names;
        private List<String> immutableNames;
        private ImmutableList<String> guavaImmutableNames;

        @BeforeEach
        void setUp() {

            this.names = new ArrayList<>();

            Arrays.stream(NAME_LIST)
                    .forEach(name -> names.add(name));
        }

        @Test
        void test_Shallow_Immutability_Of_Immutable_Lists() {

            // Given
            this.immutableNames = List.copyOf(this.names);
            this.guavaImmutableNames = ImmutableList.copyOf(this.names);

            // When
            this.names.add("Bill");
            this.names.remove("Vishwa");

            // Then
            assertThat(this.names).containsExactly("John", "Buddy", "Amy", "Bill");
            assertThat(this.immutableNames).containsExactly(NAME_LIST);
            assertThat(this.guavaImmutableNames).containsExactly(NAME_LIST);
        }

        @Test
        void test_Null_Hostility_Of_Immutable_List_Of_JDK() {

            // Given
            String newName = null;
            this.names.add(newName);

            // When
            assertThatThrownBy(() -> List.copyOf(this.names))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void test_Null_Hostility_Of_Immutable_List_Of_Guava() {

            // Given
            String newName = null;
            this.names.add(newName);

            // When
            assertThatThrownBy(() -> ImmutableList.copyOf(this.names))
                    .isInstanceOf(NullPointerException.class);
        }

    }//: End of class ImmutableListTest

    @Nested
    @DisplayName("Immutable List Test - ")
    class ImmutableMapTest {

        final String[] countries = {
                "USA", "North America",
                "Mexico", "North America",
                "Canada", "North America",
                "Costa Rica", "North America"
        };

        private Map<String, String> countryMap = new HashMap<>();
        private Map<String, String> immutableMap;
        private ImmutableMap<String, String> guavaImmutableMap;

        private UnaryOperator<Map<String, String>> immutableMapFactory =
                map -> Map.copyOf(map);

        private UnaryOperator<Map<String, String>> guavaImmutableMapFactory =
                map -> ImmutableMap.copyOf(map);

        @BeforeEach
        void setUp() {
            this.countryMap = new HashMap<>();
            this.countryMap.put(countries[0], countries[1]);
            this.immutableMapFactory = map -> Map.copyOf(map);
            this.guavaImmutableMapFactory = map -> ImmutableMap.copyOf(map);
        }

        @Test
        void test_Shallow_Immutability_Of_Immutable_Maps() {

            // Given
            this.immutableMap = Map.copyOf(this.countryMap);
            this.guavaImmutableMap = ImmutableMap.copyOf(this.countryMap);

            // When
            this.countryMap.put(this.countries[2], this.countries[3]);

            // Then
            assertThat(this.countryMap).containsKey(this.countries[2]);
            assertThat(this.immutableMap).containsOnlyKeys(this.countries[0]);
            assertThat(this.guavaImmutableMap).containsOnlyKeys(this.countries[0]);
        }

        @Test
        void test_Null_Hostility_Of_Immutable_Maps() {

            // Given
            this.countryMap.put(this.countries[2], null);

            // When & Then
            test_Null_Hostility_Of_Immutable_Maps(this.immutableMapFactory);
            test_Null_Hostility_Of_Immutable_Maps(this.guavaImmutableMapFactory);
        }

        private void test_Null_Hostility_Of_Immutable_Maps(
                UnaryOperator<Map<String, String>> immutableMapFactory) {

            assertThatThrownBy(() -> immutableMapFactory.apply(this.countryMap))
                    .isInstanceOf(NullPointerException.class);
        }


    }//: End of ImmutableMapTest

}///:~