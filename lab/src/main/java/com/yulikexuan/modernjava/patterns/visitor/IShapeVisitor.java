//: com.yulikexuan.modernjava.patterns.visitor.IShapeVisitor.java


package com.yulikexuan.modernjava.patterns.visitor;

public interface IShapeVisitor {

    String visitDot(Dot dot);
    String visitCircle(Circle circle);
    String visitRectangle(Rectangle rectangle);
    String visitCompoundGraphic(CompoundShape compoundShape);

}///:~