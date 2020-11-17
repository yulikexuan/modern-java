//: com.yulikexuan.effectivejava.enums.extension.ScienceOperationTest.java

package com.yulikexuan.effectivejava.enums.extension;


import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleBinaryOperator;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("Test extension of Enum - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ScienceOperationTest {

    private double x;
    private double y;

    private ThreadLocalRandom random;


    @BeforeEach
    void setUp() {
        this.random = ThreadLocalRandom.current();
        this.x = this.random.nextDouble(0, 100);
        this.y = this.random.nextDouble(0, 100);
    }

    @Test
    void test_Basic_Operations() {

        // Given & Then
        double[] basicOptResult = test(BasicOperation.class, x, y);
        double[] basicOptResult2 = test(Arrays.asList(BasicOperation.values()),
                x, y);

        // Then
        assertThat(basicOptResult.length).isEqualTo(
                BasicOperation.values().length);
        assertThat(basicOptResult2.length).isEqualTo(
                BasicOperation.values().length);
    }

    @Test
    void test_Science_Operations() {

        // Given & Then
        double[] scienceOptResult = test(ScienceOperation.class, x, y);
        double[] scienceOptResult2 = test(Arrays.asList(ScienceOperation.values()),
                x, y);

        // Then
        assertThat(scienceOptResult.length).isEqualTo(
                ScienceOperation.values().length);
        assertThat(scienceOptResult2.length).isEqualTo(
                ScienceOperation.values().length);
    }

    private static <T extends Enum<T> & DoubleBinaryOperator> double[] test(
            Class<T> optEnumType, double x, double y) {

        return Arrays.stream(optEnumType.getEnumConstants())
                .mapToDouble(opt -> opt.applyAsDouble(x, y))
                .toArray();
    }

    private double[] test(Collection<? extends DoubleBinaryOperator> opts,
                          double x, double y) {
        return opts.stream()
                .mapToDouble(opt -> opt.applyAsDouble(x, y))
                .toArray();
    }

}///:~