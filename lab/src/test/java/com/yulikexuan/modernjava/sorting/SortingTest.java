//: com.yulikexuan.modernjava.sorting.SortingTest.java


package com.yulikexuan.modernjava.sorting;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Java Sorting Test - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SortingTest {

    private int[] arrToSort;
    private int[] sortedArray;
    private int[] rangeSortedArray;

    @BeforeEach
    void setUp() {
        arrToSort = new int[] { 5, 1, 89, 255, 7, 88, 200, 123, 66 };
        sortedArray = new int[] {1, 5, 7, 66, 88, 89, 123, 200, 255};
        rangeSortedArray = new int[] { 5, 1, 89, 7, 88, 200, 255, 123, 66 };
    }

    @Nested
    @DisplayName("Array Sorting Test - ")
    class ArraySortingTest {

        @Test
        void test_Sorting_Array_With_Arrays_Sort_Method() {

            // Given & When
            Arrays.sort(arrToSort);

            // Then
            assertThat(arrToSort).isSorted();
            assertThat(arrToSort).isEqualTo(sortedArray);
        }

        @Test
        void test_Sorting_In_Range_With_Arrays_Sort_Method() {

            // Given & When
            Arrays.sort(arrToSort, 3, 7);

            // Then
            assertThat(arrToSort).isEqualTo(rangeSortedArray);
        }

        @Test
        void test_Sorting_Array_With_Arrays_Parallel_Sort_Method() {

            // Given & When
            Arrays.parallelSort(arrToSort);

            // Then
            assertThat(arrToSort).isSorted();
            assertThat(arrToSort).isEqualTo(sortedArray);
        }

        @Test
        void test_Partially_Sorting_With_Arrays_Parallel_Sort_Method() {

            // Given & When
            Arrays.parallelSort(arrToSort, 3, 7);

            // Then
            assertThat(arrToSort).isEqualTo(rangeSortedArray);
        }

    } //: End of class ArraySortingTest

    @Nested
    @DisplayName("Collection Sorting Test - ")
    class CollectionSortingTest {

        @Test
        void test_Sorting_List_With_Collections_Sort() {

            // Given
            List<Integer> toSortList = Arrays.stream(arrToSort)
                    .boxed().collect(Collectors.toList());

            List<Integer> expectSortedList = Ints.asList(sortedArray);

            // When
            Collections.sort(toSortList);

            // Then
            assertThat(toSortList).isEqualTo(expectSortedList);
        }

        @Test
        void test_Sorting_Linked_Hash_Set_With_Collections_Sort() {

            // Given
            Set<Integer> intSet = new LinkedHashSet<>(Ints.asList(arrToSort));
            Set<Integer> descSortedIntSet = new LinkedHashSet<>(
                    List.of(255, 200, 123, 89, 88, 66, 7, 5, 1));

            // When
            List<Integer> list = new ArrayList<>(intSet);
            Collections.sort(list, Comparator.reverseOrder());
            intSet = new LinkedHashSet<>(list);

            // Then
            assertThat(intSet).isEqualTo(descSortedIntSet);
        }

    } //: End of class CollectionSortingTest

    @Nested
    @DisplayName("Map Sorting Test - ")
    class MapSortingTest {

        private Map<Integer, String> map;

        @BeforeEach
        void setUp() {
            this.map = Map.of(
                    55, "John",
                    22, "Apple",
                    66, "Earl",
                    77, "Pearl",
                    12, "George",
                    6, "Rocky");
        }

        @Test
        void test_Map_With_Stream_Of_Map_Entry() {

            // Given & When
            Map<Integer, String> sortedkeyMap = this.map.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

            Map<Integer, String> sortedValueMap = this.map.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey,
                            Map.Entry::getValue, (e1, e2) -> e2));

            // Then
            System.out.println(sortedkeyMap);
            System.out.println(sortedValueMap);
        }

    }//: End of MapSortingTest

    @Nested
    @DisplayName("Custom Objects Sorting Test - ")
    class CustomObjectsSortingTest {

        private Employee[] employeesToSort;
        private Employee[] naturalSortedEmployees;
        private Employee[] ageSortedEmployees;
        private Employee[] ageThenNameSortedEmployees;

        private Employee john;
        private Employee steve;
        private Employee frank;
        private Employee earl;
        private Employee jessica;
        private Employee pearl;


        @BeforeEach
        void setUp() {

            this.john = Employee.builder()
                    .name("John")
                    .age(23)
                    .salary(50000)
                    .build();

            this.steve = Employee.builder()
                    .name("Steve")
                    .age(26)
                    .salary(60000)
                    .build();

            this.frank = Employee.builder()
                    .name("Frank")
                    .age(33)
                    .salary(70000)
                    .build();

            this.earl = Employee.builder()
                    .name("Earl")
                    .age(43)
                    .salary(100000)
                    .build();

            this.jessica = Employee.builder()
                    .name("Jessica")
                    .age(23)
                    .salary(40000)
                    .build();

            this.pearl = Employee.builder()
                    .name("Pearl")
                    .age(33)
                    .salary(60000)
                    .build();

            this.employeesToSort = new Employee[] {
                    john, steve, frank, earl, jessica, pearl};

            this.naturalSortedEmployees = new Employee[] {
                    earl, frank, jessica, john, pearl, steve};

            this.ageSortedEmployees = new Employee[] {
                    john, jessica, steve, frank, pearl, earl};

            this.ageThenNameSortedEmployees = new Employee[] {
                    jessica, john, steve, frank, pearl, earl};
        }

        @Test
        void test_Natural_Order_Sorting() {

            // Given & When
            Arrays.sort(employeesToSort);

            // Then
            assertThat(employeesToSort).isEqualTo(this.naturalSortedEmployees);
        }

        @Test
        void test_Sorting_With_Comparator_Comparing() {

            // Given & When
            Arrays.sort(this.employeesToSort,
                    Comparator.comparing(Employee::getAge));

            // Then
            assertThat(this.employeesToSort).isEqualTo(this.ageSortedEmployees);
        }

        @Test
        void test_Sorting_With_Comparator_Then_Comparing() {

            // Given & When
            Arrays.sort(this.employeesToSort,
                    Comparator.comparing(Employee::getAge)
                            .thenComparing(Employee::getName));

            // Then
            assertThat(this.employeesToSort).isEqualTo(
                    this.ageThenNameSortedEmployees);
        }

    }//: End of class CustomObjectsSortingTest

}///:~