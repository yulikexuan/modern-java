//: com.yulikexuan.modernjava.patterns.visitor.VisitoDemo.java


package com.yulikexuan.modernjava.patterns.visitor;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class VisitoDemo {

    public static void main(String[] args)
            throws IOException, XMLStreamException {

        Dot dot = Dot.builder().id(1).name("Dot").x(10).y(55).build();

        Circle circle = Circle.builder().id(2).x(23).y(15).radius(10)
                .name("Circle").build();

        Rectangle rectangle = Rectangle.builder()
                .id(3)
                .x(10)
                .y(17)
                .width(20)
                .height(30)
                .name("Rectangle")
                .build();

        CompoundShape compoundShape = CompoundShape.builder()
                .id(4)
                .name("CompoundShape_1")
                .build();

        compoundShape.add(dot);
        compoundShape.add(circle);
        compoundShape.add(rectangle);

        CompoundShape compoundShape2 = CompoundShape.builder()
                .id(4)
                .name("CompoundShape_2")
                .build();

        compoundShape2.add(dot);

        compoundShape.add(compoundShape2);

        XMLShapeExportVisitor visitor = new XMLShapeExportVisitor();
        String xml = visitor.export(circle, compoundShape)
                .orElse("ERROR: Exported Nothing!");
        System.out.println(xml);
    }

}///:~