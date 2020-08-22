//: com.yulikexuan.effectivejava.model.design.hierarchy.Square.java


package com.yulikexuan.effectivejava.model.design.hierarchy;


/*
 * Class hierarchy replacement for a tagged class: TaggedFigure  (Page 110-11)
 */
class Square extends Rectangle {

    public Square(double side) {
        super(side, side);
    }

}///:~