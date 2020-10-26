//: com.yulikexuan.effectivejava.generics.AsSubclassTest.java


package com.yulikexuan.effectivejava.generics;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test asSubclass method of Class<T> - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AsSubclassTest {

    @Test
    void test_Given_Subclass_When_Call_AsSubclass() {

        // Given
        Class<LinkedHashSet> linkedHashSetClass = LinkedHashSet.class;

        // When
        Class<? extends HashSet> hashSetClass = linkedHashSetClass.asSubclass(
                HashSet.class);

        // Then
        assertThat(hashSetClass).isNotEqualTo(HashSet.class);
        assertThat(hashSetClass).isEqualTo(linkedHashSetClass);
    }

}///:~