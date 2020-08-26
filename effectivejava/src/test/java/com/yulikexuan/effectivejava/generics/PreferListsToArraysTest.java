//: com.yulikexuan.effectivejava.generics.PreferListsToArraysTest.java


package com.yulikexuan.effectivejava.generics;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Lists vs Arrays Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PreferListsToArraysTest {

    private String stringElement;


    @BeforeEach
    void setUp() {
        this.stringElement = RandomStringUtils.randomAlphanumeric(30);
    }

    @Test
    void test_Given_Long_Type_Array_When_Assigning_String_Element_Then_Exception() {

        // Given
        final Object[] objArray = PreferListsToArrays.getLongTypeArray();

        // When & Then
        assertThatThrownBy(() -> objArray[0] = this.stringElement)
                .isInstanceOf(ArrayStoreException.class);
    }

    @Test
    void test_Given_Object_Type_Array_When_Assigning_String_Element() {

        // Given
        Object[] objArr = PreferListsToArrays.getObjArr();

        // When
        objArr[0] = this.stringElement;

        // Then
        assertThat(objArr[0]).isSameAs(this.stringElement);
    }

    @Test
    void test_Able_To_Assign_A_String_To_Object_List() {

        // Given
        List<Object> objList = PreferListsToArrays.getObjectTypeList();

        // When
        objList.add(this.stringElement);

        // Then
        assertThat(objList).containsExactly(this.stringElement);
    }

}///:~