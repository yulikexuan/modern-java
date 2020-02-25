//: com.yulikexuan.modernjava.concurrency.atomic.AtomicCounter.java


package com.yulikexuan.modernjava.concurrency.atomic;


import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;


public class AtomicCounter {

    private AtomicLong counter;

    private AtomicCounter() {
        this(0L);
    }

    private AtomicCounter(long initialValue) {
        this.counter = new AtomicLong(initialValue);
    }

    public static AtomicCounter of(long initialValue) {
        return new AtomicCounter(initialValue);
    }

    /*
     * Gets the value from the memory, so that changes made by other threads
     * are visible
     *
     * Equivalent to reading a volatile variable
     */
    public long getCounter() {
        return this.counter.get();
    }

    /*
     * Writes the value to memory, so that the change is visible to other threads
     * Equivalent to writing a volatile variable
     */
    public void setCounter(long newValue) {
        this.counter.set(newValue);
    }

    /*
     * Eventually writes the value to memory, may be reordered with subsequent
     * relevant memory operations
     *
     * One use case is nullifying (使無效) references, for the sake of garbage
     * collection, which is never going to be accessed again
     * In this case, better performance is achieved by delaying the null
     * volatile write
     */
    public void lazySet(long newValue) {
        this.counter.lazySet(newValue);
    }

    /*
     * Atomically sets the value to newValue if the current value == expectedValue
     */
    public boolean compareAndSet(long expectedValue, long newValue) {
        return this.counter.compareAndSet(expectedValue, newValue);
    }

    /*
     * Atomically updates the current value with the results of applying the
     * given function, returning the previous value
     *
     * The function should be side-effect-free, since it may be re-applied when
     * attempted updates fail due to contention among threads.
     */
    public long getAndUpdate(LongUnaryOperator updateFunction) {
        return this.counter.getAndUpdate(updateFunction);
    }

    public long updateAndGet(LongUnaryOperator updateFunction) {
        return this.counter.updateAndGet(updateFunction);
    }

    public long getAndAccumulate(long param,
                                      LongBinaryOperator accumulatorFunction) {

        return this.counter.getAndAccumulate(param, accumulatorFunction);
    }

    public long accumulateAndGet(long param,
                                 LongBinaryOperator accumulatorFunction) {

        return this.counter.accumulateAndGet(param, accumulatorFunction);
    }

}///:~