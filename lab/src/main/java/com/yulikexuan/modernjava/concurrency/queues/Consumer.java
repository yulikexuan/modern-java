//: com.yulikexuan.modernjava.concurrency.queues.Consumer.java


package com.yulikexuan.modernjava.concurrency.queues;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;


@Slf4j
@Builder @AllArgsConstructor
public class Consumer implements Runnable {

    static final int CONSUME_TIME_MILLI = 100;

    private final TransferQueue<String> transferQueue;
    private final String name;

    private final int numOfMsgsToConsume;
    private final AtomicInteger numOfConsumedMsgs;

    @Override
    public void run() {
        LongStream.range(0, this.numOfMsgsToConsume).forEach(
                this::consumeMessages);
    }

    private void consumeMessages(long index) {
        try {
            log.info("<<<<<<< Consumer {} is waiting to taking elements ...",
                    this.name);
            String element = this.transferQueue.take();
            this.numOfConsumedMsgs.incrementAndGet();
            Thread.sleep(CONSUME_TIME_MILLI);
            log.info("<<<<<<< Consumer {} received element: {}", this.name,
                    element);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}///:~