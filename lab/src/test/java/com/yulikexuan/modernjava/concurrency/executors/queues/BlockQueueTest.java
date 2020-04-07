//: com.yulikexuan.modernjava.concurrency.executors.queues.BlockQueueTest.java


package com.yulikexuan.modernjava.concurrency.executors.queues;


import com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceConfig;
import com.yulikexuan.modernjava.concurrency.queues.Consumer;
import com.yulikexuan.modernjava.concurrency.queues.Producer;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("TransferQueue Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BlockQueueTest {

    private Producer producer;
    private Consumer consumer;

    private TransferQueue<String> transferQueue;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        this.transferQueue = new LinkedTransferQueue<>();
        this.executorService = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    void tearDown() throws Exception {
        ExecutorServiceConfig.terminateExecutorServece(this.executorService);
    }

    @Test
    void test_Given_One_Producer_With_No_Consumer_Then_Failed_With_Timeout()
            throws Exception {

        // Given
        this.producer = Producer.builder()
                .name("P-1")
                .numOfProduceedMsgs(new AtomicInteger())
                .numOfMsgsToProduce(3)
                .transferQueue(this.transferQueue)
                .build();

        // When
        Future<?> future = this.executorService.submit(this.producer);
        future.get(); // Wait for future to be finished

        // Then
        assertThat(this.producer.getNumOfProducedMsgs()).isZero();
        assertThat(this.transferQueue.size()).isZero();
    }

    @Test
    void test_Given_One_Producer_And_One_Consumer_Then_Process_All_Messages()
            throws Exception {

        // Given
        int numberOfMessages = 3;
        this.producer = Producer.builder()
                .name("P-1")
                .numOfProduceedMsgs(new AtomicInteger())
                .numOfMsgsToProduce(numberOfMessages)
                .transferQueue(this.transferQueue)
                .build();

        this.consumer = Consumer.builder()
                .name("C-1")
                .numOfConsumedMsgs(new AtomicInteger())
                .numOfMsgsToConsume(numberOfMessages)
                .transferQueue(this.transferQueue)
                .build();

        // When
        Future<?> producerFuture = this.executorService.submit(this.producer);
        Future<?> consumerFuture = this.executorService.submit(this.consumer);

        producerFuture.get();

        // Then
        assertThat(this.producer.getNumOfProducedMsgs()).isEqualTo(
                numberOfMessages);
        assertThat(this.transferQueue.size()).isZero();
    }

}///:~