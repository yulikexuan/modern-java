//: com.yulikexuan.concurrency.design.MutablePoint.java

package com.yulikexuan.concurrency.design;


import javax.annotation.concurrent.NotThreadSafe;


/**
 * MutablePoint
 * <p/>
 * Mutable Point class similar to java.awt.Point
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class MutablePoint {

    public int x, y;

    public MutablePoint() {
        x = 0;
        y = 0;
    }

    private MutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static MutablePoint of(MutablePoint p) {
        return new MutablePoint(p.x, p.y);
    }

}///:~