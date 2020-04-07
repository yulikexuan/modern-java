//: com.yulikexuan.modernjava.concurrency.atomic.LongAdderDemo.java


package com.yulikexuan.modernjava.concurrency.atomic;


import static com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceConfig.NUMBER_OF_INCREMENTS;

import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;


public class LongAdderDemo {

    private static final LongAdder COUNTER = new LongAdder();

    private final Runnable incrementAction =
            () -> IntStream.range(0, NUMBER_OF_INCREMENTS)
            .forEach(i -> COUNTER.increment());

    private LongAdderDemo() {}

    public static LongAdderDemo of() {
        return new LongAdderDemo();
    }

    public static long sumThenReset() {
        return COUNTER.sumThenReset();
    }

    public static long sum() {
        return COUNTER.sum();
    }

    public Runnable getIncrementAction() {
        return this.incrementAction;
    }

}///:~