//: com.yulikexuan.modernjava.concurrency.atomic.Counter.java


package com.yulikexuan.modernjava.concurrency.atomic;


import lombok.Getter;


@Getter
public class Counter {

    private int counter;

    /*
     * this.counter++ may look like an atomic operation, but in fact is a
     * combination of three operations:
     *     - obtaining the value,
     *     - incrementing,
     *     - writing the updated value back
     *
     * If two threads try to get and update the value at the same time,
     * it may result in lost updates
     *
     */
    public void increment() {
        this.counter++;
    }

    /*
     * Using locks solves the problem (by using synchronized).
     * However, performance takes a hit
     *
     * When multiple threads attempt to acquire a lock, one of them wins,
     * while the rest of the threads are either blocked or suspended
     *
     * The process of suspending and then resuming a thread is very expensive
     * and affects the overall efficiency of the system
     */
    public synchronized void syncIncrement() {
        counter++;
    }

}///:~