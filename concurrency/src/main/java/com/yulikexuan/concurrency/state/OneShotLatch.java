//: com.yulikexuan.concurrency.state.OneShotLatch.java

package com.yulikexuan.concurrency.state;


import lombok.AllArgsConstructor;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;


/**
 * OneShotLatch
 * <p/>
 * Binary latch using AbstractQueuedSynchronizer
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
@AllArgsConstructor(staticName = "of")
public class OneShotLatch {

    private LongAdder waitingCount;

    private final Sync sync;

    public void signal() {
        this.sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        this.waitingCount.increment();
        this.sync.acquireSharedInterruptibly(0);
    }

    public long getWaitingCount() {
        return this.waitingCount.longValue();
    }
}


/**
 * This AQS state initially holds the latch state—closed (zero)
 */
final class Sync extends AbstractQueuedSynchronizer {

    /**
     * @return 1 if latch is oppen or a negative value to indicate acquisition
     *         failure
     */
    @Override
    protected int tryAcquireShared(int ignored) {
        // Succeed if latch is open (state == 1), else fail
        return (getState() == 1) ? 1 : -1;
    }

    /**
     * @return true if the release may let unblocked threads attempt to acquire
     *         the synchronizer
     */
    @Override
    protected boolean tryReleaseShared(int ignored) {
        setState(1); // Latch is now open
        return true; // Other threads may now be able to acquire
    }
}

///:~