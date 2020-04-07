//: com.yulikexuan.modernjava.concurrency.queues.Producer.java


package com.yulikexuan.modernjava.concurrency.queues;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;


@Slf4j
@Builder() @AllArgsConstructor
public class Producer implements Runnable {

    static final String MSG_HEADER = "M-";
    static final int TIME_OUT_MILLI = 200;

    private final TransferQueue<String> transferQueue;

    private final String name;

    private final int numOfMsgsToProduce;

    private final AtomicInteger numOfProduceedMsgs;

    @Override
    public void run() {
        LongStream.range(0, this.numOfMsgsToProduce)
                .forEach(this::produceMessages);
    }// End of run

    public int getNumOfProducedMsgs() {
        return this.numOfProduceedMsgs.intValue();
    }

    private void produceMessages(long index) {
        try {
            log.info(">>>>>>> Producer {} is waiting to transfer {} ... ",
                    this.name, index);
            boolean added = this.transferQueue.tryTransfer(MSG_HEADER + index,
                    TIME_OUT_MILLI, TimeUnit.MILLISECONDS);
            if (added) {
                this.numOfProduceedMsgs.incrementAndGet();
                log.info(">>>>>>> Producer {} transferred element: {} ",
                        this.name, MSG_HEADER + index);
            } else {
                log.info("------- Producer {} cannot add an element due to timeout.",
                        this.name);
            }
        } catch (InterruptedException ie) {
            log.error("XXXXXXX Interrupted {} XXXXXXX", index);
        }
    }

}///:~