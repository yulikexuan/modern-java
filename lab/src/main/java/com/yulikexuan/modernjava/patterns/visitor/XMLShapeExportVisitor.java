//: com.yulikexuan.modernjava.patterns.visitor.XMLShapeExportVisitor.java


package com.yulikexuan.modernjava.patterns.visitor;


import java.util.Arrays;
import java.util.Optional;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;


public class XMLShapeExportVisitor implements IShapeVisitor {

    public Optional<String> export(IShape... shapes)
            throws IOException, XMLStreamException {

        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        String shapeXML = null;

        try (StringWriter stringWriter = new StringWriter();) {

            Arrays.stream(shapes)
                    .forEach(shape -> {
                        XMLStreamWriter xmlStreamWriter = null;
                        try {
                            xmlStreamWriter =
                                    xmlOutputFactory.createXMLStreamWriter(
                                            stringWriter);
                            xmlStreamWriter.writeStartDocument();
                            xmlStreamWriter.writeCharacters(
                                    shape.accept(this));
                            xmlStreamWriter.writeEndDocument();
                        } catch (XMLStreamException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                xmlStreamWriter.close();
                            } catch (XMLStreamException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            stringWriter.flush();
            shapeXML = stringWriter.toString();
        }

        return Optional.ofNullable(shapeXML);
    }

    @Override
    public String visitDot(Dot dot) {
        return "<dot>" + "\n" +
                "    <id>" + dot.getId() + "</id>" + "\n" +
                "    <name>" + dot.getName() + "</name>" + "\n" +
                "    <x>" + dot.getX() + "</x>" + "\n" +
                "    <y>" + dot.getY() + "</y>" + "\n" +
                "</dot>";
    }

    @Override
    public String visitCircle(Circle circle) {
        return "<circle>" + "\n" +
                "    <id>" + circle.getId() + "</id>" + "\n" +
                "    <name>" + circle.getName() + "</name>" + "\n" +
                "    <x>" + circle.getX() + "</x>" + "\n" +
                "    <y>" + circle.getY() + "</y>" + "\n" +
                "    <radius>" + circle.getRadius() + "</radius>" + "\n" +
                "</circle>";
    }

    @Override
    public String visitRectangle(Rectangle rectangle) {
        return "<rectangle>" + "\n" +
                "    <id>" + rectangle.getId() + "</id>" + "\n" +
                "    <name>" + rectangle.getName() + "</name>" + "\n" +
                "    <x>" + rectangle.getX() + "</x>" + "\n" +
                "    <y>" + rectangle.getY() + "</y>" + "\n" +
                "    <width>" + rectangle.getWidth() + "</width>" + "\n" +
                "    <height>" + rectangle.getHeight() + "</height>" + "\n" +
                "</rectangle>";
    }

    @Override
    public String visitCompoundGraphic(CompoundShape compoundShape) {
        return "<compound_graphic>" + "\n" +
                "   <id>" + compoundShape.getId() + "</id>" + "\n" +
                "   <name>" + compoundShape.getName() + "</name>" + "\n" +
                _visitCompoundGraphic(compoundShape) +
                "</compound_graphic>";
    }

    private String _visitCompoundGraphic(CompoundShape cg) {
        StringBuilder sb = new StringBuilder();
        for (IShape shape : cg.getChildren()) {
            String obj = shape.accept(this);
            // Proper indentation for sub-objects.
            obj = "    " + obj.replace("\n", "\n    ") + "\n";
            sb.append(obj);
        }
        return sb.toString();
    }

}///:~