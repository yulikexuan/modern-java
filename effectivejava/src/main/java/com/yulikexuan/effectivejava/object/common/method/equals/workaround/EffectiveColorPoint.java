//: com.yulikexuan.effectivejava.object.common.method.equals.workaround.EffectiveColorPoint.java


package com.yulikexuan.effectivejava.object.common.method.equals.workaround;


import com.yulikexuan.effectivejava.object.common.method.equals.Color;
import com.yulikexuan.effectivejava.object.common.method.equals.IColorful;
import com.yulikexuan.effectivejava.object.common.method.equals.IHasCoordinate;
import com.yulikexuan.effectivejava.object.common.method.equals.Point;

import java.util.Objects;


/*
 * Adds a value component without violating the Equals contract
 */
public class EffectiveColorPoint implements IHasCoordinate, IColorful {

    private final Point point;
    private final Color color;

    private EffectiveColorPoint(Point point, Color color) {
        this.point = point;
        this.color = color;
    }

    public static EffectiveColorPoint of(Point point, Color color) {
        return new EffectiveColorPoint(Objects.requireNonNull(point),
                Objects.requireNonNull(color));
    }

    public Point asPoint() {
        return this.point;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public int getX() {
        return this.point.getX();
    }

    @Override
    public int getY() {
        return this.point.getY();
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof EffectiveColorPoint)) {
            return false;
        }

        final EffectiveColorPoint colorPoint = (EffectiveColorPoint) obj;

        return colorPoint.asPoint().equals(this.point) &&
                colorPoint.color.equals(this.color);
    }

}///:~