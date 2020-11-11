//: com.yulikexuan.effectivejava.enums.VersatileOperationTest.java


package com.yulikexuan.effectivejava.enums;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.yulikexuan.effectivejava.enums.VersatileOperation.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test versatile behaviours of Enums - ")
class VersatileOperationTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Enum_Constants_Can_Not_Access_Static_Members_From_Their_Constructors() {

        // When
        VersatileOperation plus = VersatileOperation.fromString("+")
                .get();

        // Then
        assertThat(plus).isSameAs(PLUS);
    }

}///:~