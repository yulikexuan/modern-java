//: com.yulikexuan.concurrency.thread.publication.StuffIntoPublic.java

package com.yulikexuan.concurrency.thread.publication;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;


/**
 * This improper publication could allow another thread to observe
 * a partially constructed object.
 */
@NotThreadSafe
public class StuffIntoPublic {

    public Holder holder;

    public void initialize() {
        holder = Holder.of(42);
    }
}

@ThreadSafe
final class Holder {

    @GuardedBy("this")
    private int value;

    private Holder(int value) {
        this.value = value;
    }

    public static Holder of(int value) {
        return new Holder(value);
    }

    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value) {
        this.value = value;
    }

}