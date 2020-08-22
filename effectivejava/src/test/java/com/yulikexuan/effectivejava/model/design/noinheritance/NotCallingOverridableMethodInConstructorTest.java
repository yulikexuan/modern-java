//: com.yulikexuan.effectivejava.model.design.noinheritance.NotCallingOverridableMethodInConstructorTest.java


package com.yulikexuan.effectivejava.model.design.noinheritance;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
@DisplayName("No Overridable Method Call in Constructor Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NotCallingOverridableMethodInConstructorTest {

    @Test
    void test_Throw_NPE_When_Construct_Sub() {
        assertThatThrownBy(Sub::new).isInstanceOf(NullPointerException.class);
    }

}///:~