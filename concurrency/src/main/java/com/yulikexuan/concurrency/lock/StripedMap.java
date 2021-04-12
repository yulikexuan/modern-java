//: com.yulikexuan.concurrency.lock.StripedMap.java

package com.yulikexuan.concurrency.lock;


import lombok.AllArgsConstructor;

import javax.annotation.concurrent.ThreadSafe;

/**
 * StripedMap
 * <p/>
 * Hash-based map using lock striping
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class StripedMap {

    // Synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
    private static final int N_LOCKS = 16;

    private final Node[] buckets;
    private final Object[] locks;

    public StripedMap(int numBuckets) {

        this.buckets = new Node[numBuckets];
        this.locks = new Object[N_LOCKS];

        for (int i = 0; i < N_LOCKS; i++) {
            this.locks[i] = new Object();
        }
    }

    private final int hash(Object key) {
        return Math.abs(key.hashCode() % this.buckets.length);
    }

    public Object get(Object key) {
        int hash = hash(key);
        synchronized (this.locks[hash % N_LOCKS]) {
            for (Node m = this.buckets[hash]; m != null; m = m.next) {
                if (m.key.equals(key)) {
                    return m.value;
                }
            }
        }
        return null;
    }

    public void clear() {
        for (int i = 0; i < this.buckets.length; i++) {
            synchronized (this.locks[i % N_LOCKS]) {
                this.buckets[i] = null;
            }
        }
    }

    static class Node {
        Node next;
        Object key;
        Object value;
    }

}///:~