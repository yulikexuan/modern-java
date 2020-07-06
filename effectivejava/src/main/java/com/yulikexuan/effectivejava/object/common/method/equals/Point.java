//: com.yulikexuan.effectivejava.object.common.method.equals.Point.java


package com.yulikexuan.effectivejava.object.common.method.equals;


import lombok.Getter;


@Getter
public class Point implements IHasCoordinate {

    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Point)) {
            return false;
        }

        final Point po = (Point)o;

        return po.x == x && po.y == y;
    }

//    // Broken - violates Liskov substitution principle (page 43)
//    @Override public boolean equals(Object o) {
//        if (o == null || o.getClass() != getClass())
//            return false;
//        Point p = (Point) o;
//        return p.x == x && p.y == y;
//    }

    // See Item 11
    @Override public int hashCode()  {
        return 31 * x + y;
    }

}///:~