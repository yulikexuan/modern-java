//: com.yulikexuan.concurrency.state.SleepyBoundedBuffer.java

package com.yulikexuan.concurrency.state;


import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.TimeUnit;


/**
 * SleepyBoundedBuffer
 * <p/>
 * Bounded buffer using crude blocking
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SleepyBoundedBuffer <V> extends BaseBoundedBuffer<V> {

    private static final int SLEEP_GRANULARITY = 60;

    private SleepyBoundedBuffer() {
        this(100);
    }

    protected SleepyBoundedBuffer(int size) {
        super(size);
    }

    public static <V> SleepyBoundedBuffer of(int size) {
        return new SleepyBoundedBuffer(size);
    }

    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            TimeUnit.MILLISECONDS.sleep(SLEEP_GRANULARITY);
        }
    }

    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty()) {
                    return doTake();
                }
            }
            TimeUnit.MILLISECONDS.sleep(SLEEP_GRANULARITY);
        }
    }

}///:~