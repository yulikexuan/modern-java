//: com.yulikexuan.modernjava.patterns.visitor.IShape.java


package com.yulikexuan.modernjava.patterns.visitor;


public interface IShape {

    void move(int x, int y);
    void draw();
    String accept(IShapeVisitor visitor);

}///:~