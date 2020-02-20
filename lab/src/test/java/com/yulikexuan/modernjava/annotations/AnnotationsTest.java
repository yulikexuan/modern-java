//: com.yulikexuan.modernjava.annotations.AnnotationsTest.java


package com.yulikexuan.modernjava.annotations;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AnnotationsTest {

    static final String NAME = "Yul";

    private Person person;
    private Person person2;

    @BeforeEach
    void setUp() {
        this.person = Person.of(NAME);
        this.person2 = Person.of(null);
    }

    /*
     * As of Java 8, annotations can be also applied to any type uses
     */
    @Test
    void test_NonNull_Annotation() {

        // When
        String personInfo_1 = this.person.toString();
        String personInfo_2 = this.person2.toString();

        // Then
        assertThat(personInfo_1).contains(NAME);
    }

}///:~