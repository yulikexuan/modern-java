//: com.yulikexuan.concurrency.design.Point.java

package com.yulikexuan.concurrency.design;


import lombok.Value;

import javax.annotation.concurrent.Immutable;


/**
 * Point
 * <p/>
 * Immutable Point class used by DelegatingVehicleTracker
 *
 * @author Brian Goetz and Tim Peierls
 */
@Value
@Immutable
public final class Point {

    private final int x, y;

    private Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

}///:~