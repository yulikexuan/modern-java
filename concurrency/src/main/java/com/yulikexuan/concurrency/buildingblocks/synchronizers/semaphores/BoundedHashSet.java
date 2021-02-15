//: com.yulikexuan.concurrency.buildingblocks.synchronizers.semaphores.BoundedHashSet.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers.semaphores;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;


public class BoundedHashSet<T> {

    private final Set<T> set;
    private final Semaphore semaphore;

    private BoundedHashSet(Set<T> set, Semaphore semaphore) {
        this.set = set;
        this.semaphore = semaphore;
    }

    public static <T> BoundedHashSet<T> of(int bound) {

        Set<T> set = Collections.synchronizedSet(new HashSet<T>());
        Semaphore semaphore = new Semaphore(bound);

        return new BoundedHashSet<>(set, semaphore);
    }

    public boolean add(T o) throws InterruptedException {

        this.semaphore.acquire();

        boolean wasAdded = false;

        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                semaphore.release();
            }
        }
    }

    public boolean remove(Object o) {

        boolean wasRemoved = set.remove(o);

        if (wasRemoved) {
            semaphore.release();
        }

        return wasRemoved;
    }

}///:~