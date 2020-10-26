//: com.yulikexuan.effectivejava.generics.varargs.DangerousTest.java


package com.yulikexuan.effectivejava.generics.varargs;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test using generic type with varargs - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DangerousTest {

    @Test
    void test_Store_Value_In_Generic_Varargs_Array_Param() {
        assertThatThrownBy(() -> Dangerous.dangerous(List.of("123456")))
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void test_Exception_Strace() {

        try {
            Dangerous.dangerous(List.of("123456"));
        } catch (ClassCastException e) {
            System.out.println(e.getStackTrace());
        }
    }

}///:~