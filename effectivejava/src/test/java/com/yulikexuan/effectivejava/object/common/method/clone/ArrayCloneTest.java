//: com.yulikexuan.effectivejava.object.common.method.clone.ArrayCloneTest.java


package com.yulikexuan.effectivejava.object.common.method.clone;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Equals Methods of SubClasses - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ArrayCloneTest {

    @Test
    void test_Given_A_Primitive_Type_Array_When_Cloning_Then_Get_A_Real_Copy() {

        // Given
        int[] array = {23, 43, 55, 12};

        // When
        int[] copiedArray = array.clone();

        // Then
        assertThat(copiedArray).isEqualTo(array).isNotSameAs(array);
    }

}///:~