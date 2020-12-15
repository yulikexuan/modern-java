//: com.yulikexuan.effectivejava.overloading.OverloadingWithFunctionalParamsTest.java

package com.yulikexuan.effectivejava.overloading;


import org.junit.jupiter.api.*;

import static java.lang.System.*;


@DisplayName("Test overloading with functional method reference - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OverloadingWithFunctionalParamsTest {

    @Test
    void test_Overloading_Functionally() {
        // OverloadingWithFunctionalParams.runFunctionally(out::println);
        OverloadingWithFunctionalParams.runFunctionally(() -> out.println());
    }

}///:~