//: com.yulikexuan.effectivejava.annotation.EffectiveTestAnnotationTest.java

package com.yulikexuan.effectivejava.annotation;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test Annotation EffectiveTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class EffectiveTestAnnotationTest {

    @Test
    void test_EffectiveTest_Annotation() throws Exception {

        // Given
        Class<?> testClass = AnnotationSample.class;

        // When
        Map<Boolean, Map<Boolean, List<ImmutableTriple<String, Boolean, Boolean>>>>
                processors = Arrays.stream(testClass.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(
                                EffectiveTest.class))
                        .map(AnnotationProcessors::processEffctiveTest)
                        .collect(partitioningBy(ImmutableTriple::getMiddle,
                                groupingBy(ImmutableTriple::getRight)));

        Map<Boolean, List<ImmutableTriple<String, Boolean, Boolean>>>
                failedMethods = processors.get(false);

        // Then
        assertThat(processors.get(true).size()).isEqualTo(1);
        assertThat(failedMethods.get(true).size()).isEqualTo(1);
        assertThat(failedMethods.get(false).size()).isEqualTo(2);
        assertThat(failedMethods.get(true).get(0).getLeft())
                .isEqualTo("m5");
    }

}///:~