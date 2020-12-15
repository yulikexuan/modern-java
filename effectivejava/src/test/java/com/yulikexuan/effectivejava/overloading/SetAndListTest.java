//: com.yulikexuan.effectivejava.overloading.SetAndListTest.java

package com.yulikexuan.effectivejava.overloading;


import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("Test method overloading of Set and List - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SetAndListTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void the_Final_Set_And_List_Should_Only_Conatains_Nagative_Numbers() {

        // Given & When
        Set<Integer> finalSet = SetAndList.addThenRemove().left;
        List<Integer> finalList = SetAndList.addThenRemove().right;

        // Then
        assertThat(finalSet).containsOnly(-3, -2, -1);
        assertThat(finalList).containsOnly(-3, -2, -1);
    }

}///:~