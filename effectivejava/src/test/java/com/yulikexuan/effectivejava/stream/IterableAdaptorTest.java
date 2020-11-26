//: com.yulikexuan.effectivejava.stream.IterableAdaptorTest.java

package com.yulikexuan.effectivejava.stream;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test IterableAdaptorTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class IterableAdaptorTest {

    static final int SIZE = 16;

    private Stream<String> codeStream;

    @BeforeEach
    void setUp() {
        this.codeStream = IntStream.range(0, 10)
                .mapToObj(i -> RandomStringUtils.randomAlphanumeric(SIZE));
    }

    @Test
    void not_Able_To_Iterate_Through_Stream_Directly() {
        for (String code : (Iterable<? extends String>) this.codeStream::iterator) {
            assertThat(code).hasSize(SIZE);
        }
    }

    @Test
    void able_To_Iterate_Through_Stream_With_Adaptor_Easily() {
        for (String code : IterableAdaptor.iterableOf(this.codeStream)) {
            assertThat(code).hasSize(SIZE);
        }

    }

}///:~