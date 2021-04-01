//: com.yulikexuan.concurrency.taskexe.cancellation.PrimeProducerTest.java

package com.yulikexuan.concurrency.taskexe.cancellation;


import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@DisplayName("Simple Cancellation with Interruption Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PrimeProducerTest {

    private static final int PRIME_QUEUE_CAPACITY = 6;

    private PrimeProducer primeProducer;
    private BlockingQueue<BigInteger> primeQueue;

    @BeforeEach
    void setUp() {
        this.primeQueue = new LinkedBlockingDeque<>(PRIME_QUEUE_CAPACITY);
        this.primeProducer = PrimeProducer.of(this.primeQueue);
    }

    @Test
    void should_Be_Able_To_Cancel_The_PrimeProducer_Thread()
            throws InterruptedException {

        // Given
        this.primeProducer.start();
        TimeUnit.MILLISECONDS.sleep(1000L);

        // When
        this.primeProducer.cancel();

        await().until(this.primeProducer::isCancelled);

        // Then
        assertThat(this.primeQueue.remainingCapacity()).isEqualTo(0);
    }

    @Test
    void able_To_Cancel_PrimeProducer_Task_Running_In_Executor() throws Exception {

        // Given
        ExecutorService executor =
                ExecutorServiceFactory.createSingleThreadExecutor();

        Future futurePrimes = executor.submit(this.primeProducer);
        TimeUnit.MILLISECONDS.sleep(1000L);

        // When
        futurePrimes.cancel(true);
        await().until(this.primeProducer::isCancelled);

        // Then
        assertThat(this.primeQueue.remainingCapacity()).isEqualTo(0);
    }

}///:~