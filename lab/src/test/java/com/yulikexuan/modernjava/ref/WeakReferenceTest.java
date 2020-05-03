//: com.yulikexuan.modernjava.ref.WeakReferenceTest.java


package com.yulikexuan.modernjava.ref;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.ref.WeakReference;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Java Weak Reference Test - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class WeakReferenceTest {

    static final int NAME_LENGTH = 16;

    private WeakReference<String> nameRef;

    @BeforeEach
    void setUp() {
        this.nameRef = new WeakReference(
                new String(RandomStringUtils.randomAlphanumeric(NAME_LENGTH)));
    }

    @Test
    void test_Given_Weak_Reference_Object_Then_Use_It_With_Get_Method() {

        // Given & When
        String name = this.nameRef.get();

        // we can never assume that the object we are trying to get actually exists

        // Then
        if (Objects.isNull(name)) {
            assertThat(name.length()).isEqualTo(NAME_LENGTH);
            assertThat(name).containsPattern("[a-zA-Z0-9]*");
    }
    }

}///:~