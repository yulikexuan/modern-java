//: com.yulikexuan.effectivejava.object.common.method.equals.SmellPointWithNonTransitiveEquals.java


package com.yulikexuan.effectivejava.object.common.method.equals;


public class SmellPointWithNonTransitiveEquals extends Point {

    private Smell smell;

    private SmellPointWithNonTransitiveEquals(int x, int y, Smell smell) {
        super(x, y);
        this.smell = smell;
    }

    public static SmellPointWithNonTransitiveEquals of(int x, int y, Smell smell) {
        return new SmellPointWithNonTransitiveEquals(x, y, smell);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Point)) {
            return false;
        }

        if (!(o instanceof SmellPointWithNonTransitiveEquals)) {
            return o.equals(this);
        }

        return super.equals(o) &&
                ((SmellPointWithNonTransitiveEquals) o).smell == smell;
    }

}///:~