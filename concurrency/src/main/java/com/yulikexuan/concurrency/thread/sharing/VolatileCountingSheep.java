//: com.yulikexuan.concurrency.thread.sharing.VolatileCountingSheep.java

package com.yulikexuan.concurrency.thread.sharing;


/**
 * CountingSheep
 * <p/>
 * Counting sheep
 * <p/>
 * Checking a status flag to determine when to exit a loop
 *
 * In this example, our anthropomorphized (擬人化) thread is trying to get to
 * sleep by the time-honored method of counting sheep
 *
 * For this example to work, the asleep flag must be volatile
 * Otherwise, the thread might not notice when asleep has been set by another
 * thread
 *
 * We could instead have used locking to ensure visibility of changes to asleep,
 * but that would have made the code more cumbersome
 *
 * Locking can guarantee both visibility and atomicity
 * volatile variables can only guarantee visibility
 *
 * You can use volatile variables only when all the following criteria are met
 *   1. Writes to the variable do not depend on its current value, or you can
 *      ensure that only a single thread ever updates the value
 *   2. The variable does not participate in invariants with other state
 *      variables
 *   3. Locking is not required for any other reason while the variable is
 *      being accessed
 */
public class VolatileCountingSheep {

    volatile boolean asleep;

    void tryToSleep() {
        while (!asleep) {
            countSomeSheep();
        }
    }

    void countSomeSheep() {
        // One, two, three...
    }

}///:~