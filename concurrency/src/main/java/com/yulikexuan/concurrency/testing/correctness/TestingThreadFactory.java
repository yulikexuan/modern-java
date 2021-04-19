//: com.yulikexuan.concurrency.testing.correctness.TestingThreadFactory.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;


public class TestingThreadFactory implements ThreadFactory {

    private final LongAdder numCreated = new LongAdder();
    private final ThreadFactory factory;

    private TestingThreadFactory(ThreadFactory factory) {
        this.factory = factory;
    }

    public static TestingThreadFactory of(@NonNull ThreadFactory factory) {
        return new TestingThreadFactory(factory);
    }

    @Override
    public Thread newThread(Runnable r) {
        this.numCreated.increment();
        return this.factory.newThread(r);
    }

    public long getThreadNumberOfCreated() {
        return this.numCreated.longValue();
    }

}///:~