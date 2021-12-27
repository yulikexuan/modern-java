//: com.yulikexuan.jdk.ocp2.generic.NonGenericSetTest.java

package com.yulikexuan.jdk.ocp2.generic;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DisplayName("Test NonGenericSet Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NonGenericSetTest {

    private NonGenericSet setTest;

    @BeforeEach
    void setUp() {
        this.setTest = new NonGenericSet();
    }

    @Test
    void not_safe() {
        assertThatThrownBy(() -> this.setTest.before())
                .isExactlyInstanceOf(ClassCastException.class);
    }

}///:~