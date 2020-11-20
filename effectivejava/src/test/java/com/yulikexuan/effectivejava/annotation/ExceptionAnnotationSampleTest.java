//: com.yulikexuan.effectivejava.annotation.ExceptionAnnotationSampleTest.java

package com.yulikexuan.effectivejava.annotation;


import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test ExceptionAnnotationSampleTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExceptionAnnotationSampleTest {

    @Test
    void given_AnExceptionTest_Annotation_Sample_Class_Then_Process_It() {

        // Given
        Class<?> testClass = ExceptionAnnotationSample.class;

        // When
        Map<String, ImmutableTriple<String, Boolean, Boolean>>
                processResult = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(
                        AnyExceptionTest.class))
                .map(AnnotationProcessors::processExceptionTest)
                .collect(toMap(ImmutableTriple::getLeft, r -> r));

        // Then
        assertThat(processResult.get("m1").getMiddle()).isTrue();

        assertThat(processResult.get("m2").getMiddle()).isFalse();
        assertThat(processResult.get("m2").getRight()).isFalse();

        assertThat(processResult.get("m3").getMiddle()).isFalse();
        assertThat(processResult.get("m3").getRight()).isFalse();

        assertThat(processResult.get("m4").getMiddle()).isFalse();
        assertThat(processResult.get("m4").getRight()).isTrue();

        assertThat(processResult.get("m5").getMiddle()).isTrue();
    }

}///:~