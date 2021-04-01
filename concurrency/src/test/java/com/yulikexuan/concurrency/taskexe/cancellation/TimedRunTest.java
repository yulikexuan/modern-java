//: com.yulikexuan.concurrency.taskexe.cancellation.TimedRunTest.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Future::cancel - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TimedRunTest {

    private static final int PRIME_QUEUE_CAPACITY = 6;
    private static final long RUNNING_TIMEOUT_MILLIS = 500L;

    private static ExecutorService executor;

    private BlockingQueue<BigInteger> primeQueue;
    private PrimeProducer primeProducer;
    private TimedRun timedRun;

    @BeforeAll
    static void beforeAll() throws Exception {
        executor = ExecutorServiceFactory.createSingleThreadExecutor();
    }

    @BeforeEach
    void setUp() {
        this.primeQueue = new LinkedBlockingDeque<>(PRIME_QUEUE_CAPACITY);
        this.timedRun = TimedRun.of(executor);
        this.primeProducer = PrimeProducer.of(this.primeQueue);
    }

    @Test
    void is_Able_To_Cancel_Prime_Producer_With_The_Future_Object() throws Exception {

        // Given & When
        Future<?> timedRunFuture = this.timedRun.timedRun(this.primeProducer,
                RUNNING_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        // When
        boolean isPrimeProducerCancelled = timedRunFuture.isCancelled();

        // Then
        assertThat(isPrimeProducerCancelled).isTrue();
        assertThat(this.primeProducer.isCancelled()).isTrue();
    }

}///:~