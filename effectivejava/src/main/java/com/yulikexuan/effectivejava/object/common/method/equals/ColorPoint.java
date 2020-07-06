//: com.yulikexuan.effectivejava.object.common.method.equals.ColorPoint.java


package com.yulikexuan.effectivejava.object.common.method.equals;


import lombok.Getter;


@Getter
public class ColorPoint extends Point implements IColorful{

    private Color color;

    private ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    public static ColorPoint of(int x, int y, Color color) {
        return new ColorPoint(x, y, color);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof ColorPoint)) {
            return false;
        }

        return super.equals(o) && ((ColorPoint) o).color == color;
    }

}///:~