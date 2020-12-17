//: com.yulikexuan.effectivejava.optionals.ListOfOptionalsHandlerTest.java

package com.yulikexuan.effectivejava.optionals;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("Test Optional::Stream Method - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ListOfOptionalsHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void given_List_Of_Optionals_Of_String_Then_Get_List_Of_String_Back() {

        // Given
        int count = 30;
        List<Optional<String>> possibilities = List.of(
                Optional.of(RandomStringUtils.randomAlphanumeric(count)),
                Optional.of(RandomStringUtils.randomAlphanumeric(count)),
                Optional.of(RandomStringUtils.randomAlphanumeric(count)),
                Optional.empty(),
                Optional.of(RandomStringUtils.randomAlphanumeric(count)),
                Optional.of(RandomStringUtils.randomAlphanumeric(count)));

        // When
        List<String> items = ListOfOptionalsHandler.transform(possibilities);

        // Then
        assertThat(items).hasSize(possibilities.size() - 1);
        for (String item : items) {
            assertThat(item).hasSize(count);
        }

    }

}///:~