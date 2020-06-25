//: com.yulikexuan.effectivejava.object.construction.ReusingExpensiveObjectTest.java


package com.yulikexuan.effectivejava.object.construction;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test reusing heavy objects - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReusingExpensiveObjectTest {

    private static final String[] ROMAN_NUMBERS = {
            "II",
            "III",
            "IV",
            "V",
            "VI",
            "VII",
            "VIII",
            "IX",
            "LXX",
            "LXXX"
    };

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Performance_Of_Reusing_Expensive_Object() {

        // Given
        StopWatch stopWatch = StopWatch.createStarted();

        // When
        boolean allRomanNumeral = IntStream.range(0, ROMAN_NUMBERS.length)
                .mapToObj(i -> ROMAN_NUMBERS[i])
                .filter(RomanNumerals::isRomanNumeralSlowly)
                .findAny()
                .isPresent();

        stopWatch.suspend();
        long elapseNano = stopWatch.getNanoTime();
        log.info(">>>>>>> Elapsed of Not Reusing - {} nano.", elapseNano);

        stopWatch.resume();
        boolean allRomanNumeral_2 = IntStream.range(0, ROMAN_NUMBERS.length)
                .mapToObj(i -> ROMAN_NUMBERS[i])
                .filter(RomanNumerals::isRomanNumeral)
                .findAny()
                .isPresent();
        stopWatch.stop();

        long elapseNano_2 = stopWatch.getNanoTime();
        log.info(">>>>>>> Elapsed of Reusing - {} nano.", elapseNano_2 - elapseNano);

        // Then
        assertThat(allRomanNumeral).isTrue();
    }

}///:~