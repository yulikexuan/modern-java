//: com.yulikexuan.concurrency.design.SafePoint.java

package com.yulikexuan.concurrency.design;


import lombok.NonNull;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;


/**
 * SafePoint
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SafePoint {

    @GuardedBy("this")
    private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    private SafePoint(SafePoint p) {
        this(p.get());
    }

    private SafePoint(int x, int y) {
        this.set(x, y);
    }

    public static SafePoint of(@NonNull SafePoint safePoint) {
        return new SafePoint(safePoint);
    }

    public static SafePoint of(int x, int y) {
        return new SafePoint(x, y);
    }

    public synchronized int[] get() {
        return new int[] {x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

}///:~