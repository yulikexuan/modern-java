//: com.yulikexuan.effectivejava.stream.parallel.ParallelMersennePrimesFactoryTest.java

package com.yulikexuan.effectivejava.stream.parallel;


import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test Mersenne Primes Generation in Paralle - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ParallelMersennePrimesFactoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void the_Number_Of_Primes_Should_Be_Limited() {
        assertThatThrownBy(() -> ParallelMersennePrimesFactory
                .getMersennePrimesInParallel(
                        ParallelMersennePrimesFactory.MAX_LIMIT + 1))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("Too Many");
    }

    @Test
    void parallelizing_The_Pipeline_Will_Spend_Much_More_Time() {

        // Given
        int limit = 7;
        StopWatch stopWatch = StopWatch.create();

        // When
        stopWatch.start();
        List<BigInteger> mersennePrimesByParallel = ParallelMersennePrimesFactory
                .getMersennePrimesInParallel(limit);
        stopWatch.stop();
        long parallelTime = stopWatch.getTime(TimeUnit.MILLISECONDS);

        stopWatch.reset();
        stopWatch.start();
        List<BigInteger> mersennePrimes = ParallelMersennePrimesFactory
                .getMersennePrimes(limit);
        stopWatch.stop();
        long time = stopWatch.getTime(TimeUnit.MILLISECONDS);

        // Then
        assertThat(mersennePrimesByParallel).hasSize(limit);
        assertThat(mersennePrimes).hasSize(limit);
        assertThat(parallelTime / (time + 1)).isGreaterThan(100);
    }

}///:~