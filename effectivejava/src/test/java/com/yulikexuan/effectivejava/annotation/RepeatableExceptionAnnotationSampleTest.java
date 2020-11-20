//: com.yulikexuan.effectivejava.annotation.RepeatableExceptionAnnotationSampleTest.java


package com.yulikexuan.effectivejava.annotation;


import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test RepeatableExceptionAnnotationSampleTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RepeatableExceptionAnnotationSampleTest {

    @Test
    void given_Method_Annotated_By_Multi_AnExceptionTest_Then_Process() {

        // Given
        Class<?> testClass = RepeatableExceptionAnnotationSample.class;

        // When
        Map<String, ImmutableTriple<String, Boolean, Boolean>> results =
                    Arrays.stream(testClass.getDeclaredMethods())
                    .map(AnnotationProcessors::processRepeatableExceptionTest)
                    .collect(ImmutableMap.toImmutableMap(
                            ImmutableTriple::getLeft, it -> it));

        // Then
        assertThat(results.get("m0").getMiddle()).isTrue();
        assertThat(results.get("m1").getMiddle()).isFalse();
    }

}///:~