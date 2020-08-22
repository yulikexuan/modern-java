//: com.yulikexuan.effectivejava.model.design.hierarchy.Rectangle.java


package com.yulikexuan.effectivejava.model.design.hierarchy;


import lombok.AllArgsConstructor;


/*
 * Class hierarchy replacement for a tagged class: TaggedFigure  (Page 110-11)
 */
class Rectangle extends AbstractFigure {

    private final double length;
    private final double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() {
        return this.length * this.width;
    }

}///:~