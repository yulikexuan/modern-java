//: com.yulikexuan.modernjava.patterns.visitor.Circle.java


package com.yulikexuan.modernjava.patterns.visitor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder @AllArgsConstructor
public class Circle implements IShape {

    private int id;
    private int x;
    private int y;
    private int radius;
    private String name;

    @Override
    public String accept(IShapeVisitor visitor) {
        return visitor.visitCircle(this);
    }

    @Override
    public void move(int x, int y) {
        System.out.println(this.name + " is moving.");
    }

    @Override
    public void draw() {
        System.out.println("Drawing " + this.name);
    }

}///:~