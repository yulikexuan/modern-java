//: com.yulikexuan.modernjava.comparators.ComparatorTest.java


package com.yulikexuan.modernjava.comparators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.*;
import static org.assertj.core.api.Assertions.assertThat;


public class ComparatorTest {

    /*
     * inventory.sort(comparing(Apple::getWeight));
     * Comparator<Apple> c = Comparator.comparing(Apple::getWeight);
     *
     * List<Trader> traders =
transactions.stream()
.map(Transaction::getTrader)
.filter(trader -> trader.getCity().equals("Cambridge"))
.distinct()
.sorted(comparing(Trader::getName))
.collect(toList());
*
* Map<Dish.Type, Optional<Dish>> mostCaloricByType =
menu.stream()
.collect(groupingBy(Dish::getType,
maxBy(comparingInt(Dish::getCalories))));
*
* Two new utilities let you sort the entries of a map by values or keys:
 Entry.comparingByValue
 Entry.comparingByKey
*
* Collections.sort(persons, comparing(p -> p.getAge()));
*
* persons.sort(comparing(Person::getAge)
.thenComparing(Person::getName));
*
     */

    private List<Human> humans;
    private ArrayList<Human> humanRes;

    @BeforeEach
    void setUp() {
        this.humans = List.of(
                Human.builder().age(80).name("Jobs").personality(Personality.of(
                        "Buddhism", "Negative")).build(),
                Human.builder().age(10).name("Sarah").personality(Personality.of(
                        "Christianity", "Positive")).build(),
                // Human.of("Jack", 12),
                Human.builder().age(12).name("Jack").personality(Personality.of(
                        "Catholic", "Positive")).build(),
                Human.builder().age(80).name("Gates").personality(Personality.of(
                        "", "Atheist")).build());

        this.humanRes = Lists.newArrayList(this.humans);
    }

    private List<String> getNames(@NonNull List<Human> humanRes) {
        return humanRes.stream()
                .map(Human::getName)
                .collect(ImmutableList.toImmutableList());
    }

    @Nested
    @DisplayName("Comparator::comparing Tests - ")
    class ComparingTest {

        /*
         * Test:
         * static <T,​U extends Comparable<? super U>> Comparator<T> comparing​(Function<? super T,​? extends U> keyExtractor)
         */
        @Test
        void test_Comparing() {

            // Given

            // When
            humanRes.sort(comparingInt(Human::getAge));

            List<String> names = getNames(humanRes);

            // Then
            assertThat(names).containsSequence("Sarah", "Jack", "Jobs", "Gates");
        }

        @Test
        void test_Reversed_Comparing() {

            // Given

            // When
            humanRes.sort(comparing(Human::getAge).reversed());

            List<String> names = getNames(humanRes);

            // Then
            assertThat(names).containsSequence( "Jobs", "Gates", "Jack", "Sarah");
        }

        @Test
        void test_Then_Comparing() {

            // Given

            // When
            humanRes.sort(comparing(Human::getAge).reversed()
                    .thenComparing(Human::getName));

            List<String> names = getNames(humanRes);

            // Then
            assertThat(names).containsSequence("Gates", "Jobs", "Jack", "Sarah");
        }

        @Test
        void test_Comparing_With_Custom_Comparator() {

            // Given

            // When
            humanRes.sort(comparing(Human::getPersonality,
                    comparing(Personality::getFaith)));

            // Then
            assertThat(getNames(humanRes)).containsSequence(
                    "Gates", "Jobs", "Jack", "Sarah");
        }

    }//: End of class ComparingTest

    @Nested
    @DisplayName("Natural / Reversed Order of Comparable Object Test ")
    class NaturalAndReversedOrderTest {

        /*
         * Comparator::
         * static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
         * Returns a comparator that compares Comparable objects in natural order
         * Comparable Object must implement Comparable<T> interface
         */
        @Test
        void test_Comparing_With_Natural_Order() {

            // Given

            // When
            humanRes.sort(naturalOrder());

            // Then
            assertThat(getNames(humanRes)).containsSequence(
                    "Gates", "Jack", "Jobs", "Sarah");
        }

        /*
         * Comparator::
         * static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
         * Returns a comparator that compares Comparable objects in reversed natural
         * order
         * Comparable Object must implement Comparable<T> interface
         */
        @Test
        void test_Comparing_With_Reversed_Order() {

            // Given

            // When
            humanRes.sort(reverseOrder());

            // Then
            // Then
            assertThat(getNames(humanRes)).containsSequence(
                    "Sarah", "Jobs", "Jack", "Gates");
        }

    }//: End of class NaturalAndReversedOrderTest

    @Nested
    @DisplayName("Null Values Controll Test - ")
    class NullValuesInComparatorTest {

        /*
         * The nullsFirst function will return a Comparator that keeps all
         * nulls at the beginning of the ordering sequence
         */
        @Test
        void test_Null_First_Comparing() {

            // Given
            humanRes.add(1, null);
            humanRes.add(3, null);

            // When
            humanRes.sort(nullsFirst(comparing(Human::getName)));

            // Then
            assertThat(humanRes).startsWith(null, null);
        }

        @Test
        void test_Null_Last_Comparing() {

            // Given
            humanRes.add(1, null);
            humanRes.add(3, null);

            // When
            humanRes.sort(nullsLast(comparing(Human::getName)));

            // Then
            assertThat(humanRes).contains(null, Index.atIndex(4));
            assertThat(humanRes).contains(null, Index.atIndex(5));
        }

    }//: End of class NullValuesInComparatorTest

}///:~