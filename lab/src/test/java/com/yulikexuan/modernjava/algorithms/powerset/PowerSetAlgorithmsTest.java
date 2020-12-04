//: com.yulikexuan.modernjava.algorithms.powerset.PowerSetAlgorithmsTest.java

package com.yulikexuan.modernjava.algorithms.powerset;


import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test different PowerSet Algorithms - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PowerSetAlgorithmsTest {

    private Set<String> names;
    private int expectedPowerSetSize;
    private int expectedNameFreq;

    @BeforeEach
    void setUp() {
        names = Set.of("APPLE", "ORANGE", "BANANA", "PINEAPPLE");
        int size = names.size();
        expectedPowerSetSize = 1 << size;
        expectedNameFreq = 1 << size - 1;
    }

    private void assertPowerSet(Set<Set<String>> namePowerSet) {
        assertThat(namePowerSet).hasSize(expectedPowerSetSize);
        Map<String, Integer> nameFrequency = PowerSetAlgorithms
                .getElementFrequencyMap(namePowerSet);
        nameFrequency.forEach((name, freq) -> assertThat(freq)
                .isEqualTo(expectedNameFreq));
    }

    @Nested
    @DisplayName("Test powerset algorithms learning path - ")
    class LearningPathTest {
        @Test
        void able_To_Generate_Power_Set_With_Guava_Easily() {

            // Given & Then
            Set<Set<String>> namePowerSet = PowerSetAlgorithms.getGuavaPowerSet(
                    names);

            // Then
            assertPowerSet(namePowerSet);
        }

        @Test
        void able_To_Generate_Power_Set_From_Scratch() {

            // Given & Then
            Set<Set<String>> namePowerSet =
                    PowerSetAlgorithms.getPowerSetRecursively(names);

            // Then
            assertPowerSet(namePowerSet);
        }

        @Test
        void able_To_Generate_PowerSet_Using_Element_Index() {

            // Given & Then
            Set<Set<String>> namePowerSet = PowerSetAlgorithms
                    .getIndexPowerSetRecursively(names);

            // Then
            assertPowerSet(namePowerSet);
        }

        @Test
        void able_To_Generate_PowerSet_Through_Binary_List() {

            // Given & Then
            Set<Set<String>> namePowerSet = PowerSetAlgorithms
                    .getBinaryPowerSetRecursively(names);

            // Then
            assertPowerSet(namePowerSet);
        }

    }

    @Nested
    @DisplayName("Test optimized powerset algorithms - ")
    class OptimizedPowerSetFactoryTest {

        @Test
        void able_To_Generate_PowerSet_Binary_Gray_Code() {

            // Given & Then
            Set<Set<String>> namePowerSet = PowerSetAlgorithms
                    .getBinaryGraycodePowerSetRecursively(names);

            // Then
            assertPowerSet(namePowerSet);
        }

        @Test
        void test_Bit_Count() {
            int count = Integer.bitCount(7);
            System.out.println(count);
        }
    }


}///:~