//: com.yulikexuan.concurrency.buildingblocks.HiddenIterator.java

package com.yulikexuan.concurrency.buildingblocks;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * HiddenIterator
 * <p/>
 * Iteration hidden within string concatenation
 *
 * The real lesson here is that the greater the distance between the state and
 * the synchronization that guards it, the more likely that someone will forget
 * to use proper synchronization when accessing that state
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class HiddenIterator {

    @GuardedBy("this")
    private final Set<Integer> set = new HashSet<Integer>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    /*
     * The addTenThings method could throw ConcurrentModificationException,
     * because the collection is being iterated by toString in the process of
     * preparing the debugging message
     */
    public void addTenThings() {

        ThreadLocalRandom r = ThreadLocalRandom.current();

        for (int i = 0; i < 10; i++) {
            add(r.nextInt());
        }

        System.out.println("DEBUG: added ten elements to " + set);
    }

}///:~