//: com.yulikexuan.concurrency.taskexe.cancellation.PrimeGeneratorIT.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Cancellation Policy Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PrimeGeneratorIT {

    private static StopWatch stopWatch;
    private static ExecutorService executor;

    @BeforeAll
    static void beforeAll() throws Exception {
        stopWatch = StopWatch.create();
        int cpuCount = Runtime.getRuntime().availableProcessors();
        executor = ExecutorServiceFactory.createFixedPoolSizeExecutor(cpuCount);
    }

    @BeforeEach
    void setUp() throws Exception {
        stopWatch.reset();
    }

    @Test
    void able_To_Cancel_The_Prime_Generation_Task_In_Any_Specified_Duration() throws InterruptedException {

        // Given
        long millis = 500L;
        Duration cancelTime = Duration.ofMillis(millis);

        stopWatch.start();

        // When
        List<BigInteger> primes = PrimeGenerator.aDurationOfPrimes(
                executor, cancelTime);
        long actualBgTaskMillis = stopWatch.getTime();
        Duration actualDuration = Duration.ofMillis(actualBgTaskMillis);

        // Then
        log.info(">>>>>> {} primes were generated.", primes.size());
        assertThat(primes).isNotEmpty();
        assertThat(actualDuration).isBetween(
                cancelTime, cancelTime.plusMillis(100L));
    }

}///:~