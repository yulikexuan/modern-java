//: com.yulikexuan.effectivejava.libraries.CurlTest.java

package com.yulikexuan.effectivejava.libraries;


import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@Disabled
@DisplayName("Test Curl Functionality - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CurlTest {

    @Test
    void given_Url_The_Print_Its_Content() throws IOException {

        // Given
        String url = "http://dummy.restapiexample.com/api/v1/employee/1";

        // When
        String employee = Curl.printContentOfUrl(url);

        // Then
        assertThat(employee).isNotBlank();
    }

}///:~