//: com.yulikexuan.effectivejava.object.common.method.equals.ColorPointWithNonSymmetryEquals.java


package com.yulikexuan.effectivejava.object.common.method.equals;


public class ColorPointWithNonSymmetryEquals extends Point {

    private Color color;

    private ColorPointWithNonSymmetryEquals(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    public static ColorPointWithNonSymmetryEquals of(int x, int y, Color color) {
        return new ColorPointWithNonSymmetryEquals(x, y, color);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof ColorPoint)) {
            return false;
        }

        return super.equals(o) &&
                ((ColorPointWithNonSymmetryEquals) o).color == color;
    }

}///:~