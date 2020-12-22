//: com.yulikexuan.effectivejava.strings.InterviewTest.java

package com.yulikexuan.effectivejava.strings;


import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test InterviewTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InterviewTest {


    @BeforeEach
    void setUp() {

    }

    @Test
    void test_Permutation() {

        // Given

        // When
        assertThat(Interview.isPermutation(List.of('l')))
                .isTrue();
        assertThat(Interview.isPermutation(List.of('l', 'l')))
                .isTrue();
        assertThat(Interview.isPermutation(List.of('l', 'a')))
                .isFalse();
        assertThat(Interview.isPermutation(List.of('l', 'l', 'a', 'a', 'v', 'v')))
                .isTrue();
        assertThat(Interview.isPermutation(List.of('l', 'l', 'a', 'a', 'v')))
                .isTrue();
        assertThat(Interview.isPermutation(List.of(
                'v', 'v', 'v', 'v', 'v')))
                .isTrue();
        assertThat(Interview.isPermutation(List.of()))
                .isTrue();
    }

}///:~