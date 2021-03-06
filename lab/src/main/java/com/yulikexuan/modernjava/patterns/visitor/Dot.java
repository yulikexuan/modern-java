//: com.yulikexuan.modernjava.patterns.visitor.Dot.java


package com.yulikexuan.modernjava.patterns.visitor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder @AllArgsConstructor
public class Dot implements IShape {

    private int id;
    private int x;
    private int y;
    private String name;

    @Override
    public void move(int x, int y) {
        System.out.println(this.name + " is moving.");
    }

    @Override
    public void draw() {
        System.out.println("Drawing " + this.name);
    }

    @Override
    public String accept(IShapeVisitor visitor) {
        return visitor.visitDot(this);
    }

}///:~