//: com.yulikexuan.modernjava.patterns.visitor.CompoundShape.java


package com.yulikexuan.modernjava.patterns.visitor;


import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class CompoundShape implements IShape {

    private int id;
    private String name;
    private List<IShape> children = new ArrayList<>();

    @Builder
    public CompoundShape(int id, String name) {
        this.id = id;
        this.name = name;
    }

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
        return visitor.visitCompoundGraphic(this);
    }

    public void add(IShape shape) {
        children.add(shape);
    }

}///:~