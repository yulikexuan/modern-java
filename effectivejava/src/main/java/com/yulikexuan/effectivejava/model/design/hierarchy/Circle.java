//: com.yulikexuan.effectivejava.model.design.hierarchy.Circle.java


package com.yulikexuan.effectivejava.model.design.hierarchy;


import lombok.AllArgsConstructor;


/*
 * Class hierarchy replacement for a tagged class: TaggedFigure  (Page 110-11)
 */
@AllArgsConstructor(staticName = "of")
final class Circle extends AbstractFigure {

    private final double radius;

    @Override
    double area() {
        return 0;
    }

}///:~