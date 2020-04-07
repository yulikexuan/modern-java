//: com.yulikexuan.modernjava.concurrency.atomic.LongAccumulatorDemo.java


package com.yulikexuan.modernjava.concurrency.atomic;


import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;
import static com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceConfig.*;


public class LongAccumulatorDemo {

    private final static LongAccumulator ACCUMULATOR =
            new LongAccumulator(Long::sum, 0L);

    private Runnable accumulateAction = () ->
            IntStream.rangeClosed(0, NUMBER_OF_INCREMENTS)
                    .forEach(ACCUMULATOR::accumulate);

    public static long get() {
        return ACCUMULATOR.get();
    }

    public static long getThenReset() {
        return ACCUMULATOR.getThenReset();
    }

    public Runnable getAccumulateAction() {
        return this.accumulateAction;
    }

}///:~