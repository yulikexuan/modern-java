//: com.yulikexuan.modernjava.fp.ListSubsetsTest.java


package com.yulikexuan.modernjava.fp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("Subsets of a List Test - ")
class ListSubsetsTest {

    private List<Integer> list;
    private List<List<Integer>> result;

    @BeforeEach
    void setUp() {
    }

    /*
     * Reference Resource Url:
     * "https://stackoverflow.com/questions/8817608/concurrentmodificationexception-thrown-by-sublist"
     */
    @Nested
    @DisplayName("List::subList Test - ")
    class SubListTest {

        private List<String> originalList;
        private List<String> subList;

        @BeforeEach
        void setUp() {
            this.originalList = new ArrayList<>();
            this.originalList.addAll(Arrays.asList(
                    "Delhi",
                    "Bangalore",
                    "New York",
                    "London",
                    "Montréal"
            ));

            // ["Bangalore", "New York"]
            this.subList = this.originalList.subList(1, 3);
        }

        @Test
        @DisplayName("Test performing non-structural changes in original list - ")
        void test_Non_Structural_Change_On_Original_List() {

            // When
            Collections.swap(this.originalList, 0, 1);

            // Then
            assertThat(this.subList)
                    .as("Non-structural changes in original list " +
                            "should be reflected in sub list.")
                    .containsExactly("Delhi", "New York");
        }

        @Test
        @DisplayName("Test performing non-structural changes in sub list - ")
        void test_Non_Structural_Change_On_Sub_List() {

            // When
            Collections.swap(this.subList, 0, 1);

            // Then
            assertThat(this.originalList)
                    .as("Non-structural changes in sub-list " +
                            "should be reflected in the original list.")
                    .containsExactly("Delhi", "New York", "Bangalore",
                            "London", "Montréal");
        }

        @Test
        @DisplayName("Test performing structural changes in original list - ")
        void test_Structural_Change_On_Original_List() {

            // When
            this.originalList.add("Toronto");

            // Then
            assertThatThrownBy(() -> this.subList.size())
                    .as("Any structural change made on the original" +
                            "list can result in throwing ConcurrentModificationException" +
                            "from the operation of sub-list")
                    .isInstanceOf(ConcurrentModificationException.class);
        }

        @Test
        @DisplayName("Test performing structural changes in sub-list - ")
        void test_Structural_Change_On_Sub_List() {

            // When
            this.subList.add("Toronto");

            // Then
            assertThat(this.originalList)
                    .as("Structural changes in sub-list " +
                            "should be reflected in the original list.")
                    .containsExactly("Delhi", "Bangalore",  "New York",
                            "Toronto", "London", "Montréal");
        }

    } //: End of class SubListTest

    @Test
    @DisplayName("Test Subsets of an empty List - ")
    void test_Subsets_Of_Empty_Or_Null_List() {

        // Given

        // When

        // Then
        assertAll("Null List and Empty List have same subsets",
                () -> assertThat(ListSubsets.subsets(List.of()))
                        .as("Should only contain an empty List of Integer")
                        .hasSize(1)
                        .contains(List.of()),
                () -> assertThat(ListSubsets.subsets(null))
                        .as("Should only contain an empty List of Integer")
                        .hasSize(1)
                        .contains(List.of()));
    }

    @Test
    void test_Sub_List_Of_A_List_With_One_Element() {

         // Given
        list = List.of(9);

        // When
        List<List<Integer>> subsets = ListSubsets.subsets(this.list);

        // Then
        assertThat(subsets).containsExactly(List.of(), List.of(9));
    }

    @Test
    void test_Sub_List_Of_A_List_With_Two_Element() {

        // Given
        list = List.of(4, 9);

        // When
        List<List<Integer>> subsets = ListSubsets.subsets(this.list);

        // Then
        //System.out.println(subsets);
        assertThat(subsets).contains(List.of(), List.of(9), List.of(4),
                List.of(4, 9));
    }

    @Test
    void test_Sub_List_Of_A_List_With_Three_Element() {

        // Given
        list = List.of(1, 4, 9);

        // When
        List<List<Integer>> subsets = ListSubsets.subsets(this.list);

        // Then
//        System.out.println(subsets);
        assertThat(subsets).contains(
                List.of(),
                List.of(9),
                List.of(4),
                List.of(4, 9),
                List.of(1),
                List.of(1, 9),
                List.of(1, 4),
                List.of(1, 4, 9));
    }

}///:~