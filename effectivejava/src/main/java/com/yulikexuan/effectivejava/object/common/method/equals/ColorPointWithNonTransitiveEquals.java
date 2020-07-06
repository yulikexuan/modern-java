//: com.yulikexuan.effectivejava.object.common.method.equals.ColorPointWithNonTransitiveEquals.java


package com.yulikexuan.effectivejava.object.common.method.equals;


public class ColorPointWithNonTransitiveEquals extends Point {

    private Color color;

    private ColorPointWithNonTransitiveEquals(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    public static ColorPointWithNonTransitiveEquals of(int x, int y, Color color) {
        return new ColorPointWithNonTransitiveEquals(x, y, color);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Point)) {
            return false;
        }

        if (!(o instanceof ColorPointWithNonTransitiveEquals)) {
            return o.equals(this);
        }

        return super.equals(o) &&
                ((ColorPointWithNonTransitiveEquals) o).color == color;
    }

}///:~