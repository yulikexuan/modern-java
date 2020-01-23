//: com.yulikexuan.modernjava.stax.StAXDemoTest.java


package com.yulikexuan.modernjava.stax;


import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;

import javax.xml.stream.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StAXDemoTest {

    static final String LINE_SEPARATOR = System.getProperty("line.separator");
    static final String RECIPE_XML = "<?xml version=\"1.0\" ?><h:html xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:t=\"http://www.tecsys.com\"><h:head><h:title>Recipe</h:title></h:head><h:body><t:recipe><t:title>Grilled Cheese Sandwich</t:title><t:ingredients><h:ul><h:li><t:ingredient qty=\"2\">bread slice</t:ingredient></h:li></h:ul></t:ingredients></t:recipe></h:body></h:html>";

    private static Path outputFilePath;


    @BeforeAll
    static void beforeAll() throws URISyntaxException, IOException {
        outputFilePath = StAXDemo.getOutputFilePath();
    }

    @BeforeEach
    void setUp() throws URISyntaxException {
    }

    @Test
    @Order(2)
    @DisplayName("Test XML Stream Reader of StAX - ")
    void test_Xml_Stream_Reader() {

        assumeTrue(Files.exists(outputFilePath));

        // Given
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLStreamReader xmlStreamReader = null;
        StringBuilder xmlOutputBuilder = new StringBuilder();
        try (FileReader fileReader = new FileReader(outputFilePath.toFile())) {
            xmlStreamReader = xmlInputFactory.createXMLStreamReader(fileReader);
            while (xmlStreamReader.hasNext()) {
                switch (xmlStreamReader.next()) {
                    case XMLStreamReader.START_ELEMENT:
                        xmlOutputBuilder.append("START_ELEMENT");
                        xmlOutputBuilder.append(String.format(
                                "%n    Qname = %s%n",
                                xmlStreamReader.getName()));
                        break;
                    case XMLStreamReader.CHARACTERS:
                        String value = xmlStreamReader.getText();
                        if (!value.trim().isBlank()) {
                            xmlOutputBuilder.append(String.format(
                                    "    Value: %s%n", value));
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        xmlOutputBuilder.append("END_ELEMENT");
                        xmlOutputBuilder.append(String.format(
                                "%n    Qname = %s%n",
                                xmlStreamReader.getName()));
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            if (xmlStreamReader != null) {
                try {
                    xmlStreamReader.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        }
        String outputXml = xmlOutputBuilder.toString();
        System.out.println(outputXml);

        // When
        int rootAppearance = StringUtils.countMatches(outputXml,
                "Qname = {http://www.w3.org/1999/xhtml}html");
        int ingredientElementsAppearance = StringUtils.countMatches(outputXml,
                "Qname = {http://www.tecsys.com}ingredient");
        int recipeAppearance = StringUtils.countMatches(outputXml,
                "Qname = {http://www.tecsys.com}recipe");

        // Then
        assertThat(rootAppearance).isEqualTo(2);
        assertThat(ingredientElementsAppearance).isEqualTo(4);
        assertThat(recipeAppearance).isEqualTo(2);
    }

    private void writeRecipeXml(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        // Start Document: <?xml version="1.0" ?>
        xmlStreamWriter.writeStartDocument();

        // Start <h:html>
        xmlStreamWriter.setPrefix("h", StAXDemo.NAMESPACE_1);
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_1,
                "html");
        xmlStreamWriter.writeNamespace("h", StAXDemo.NAMESPACE_1);
        xmlStreamWriter.writeNamespace("t", StAXDemo.NAMESPACE_2);

        // Start <h:head>
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_1,
                "head");
        // Start <h:title>
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_1,
                "title");
        xmlStreamWriter.writeCharacters("Recipe");
        xmlStreamWriter.writeEndElement(); // End of <h:title>
        xmlStreamWriter.writeEndElement(); // End of <h:header>

        // Start of <h:body>
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_1,
                "body");

        // Start of <t:recipe>
        xmlStreamWriter.setPrefix("t", StAXDemo.NAMESPACE_2);
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_2,
                "recipe");

        // Start of <t:title>
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_2,
                "title");
        xmlStreamWriter.writeCharacters("Grilled Cheese Sandwich");
        xmlStreamWriter.writeEndElement(); // End of <t:title>

        // Start of <t:ingredients>
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_2,
                "ingredients");

        // Start of <h:ul><h:li>
        xmlStreamWriter.setPrefix("h", StAXDemo.NAMESPACE_1);
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_1, "ul");
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_1, "li");

        xmlStreamWriter.setPrefix("t", StAXDemo.NAMESPACE_2);
        xmlStreamWriter.writeStartElement(StAXDemo.NAMESPACE_2,
                "ingredient");
        xmlStreamWriter.writeAttribute("qty", "2");
        xmlStreamWriter.writeCharacters("bread slice");
        xmlStreamWriter.writeEndElement(); // End of <t:ingredient>

        xmlStreamWriter.setPrefix("h", StAXDemo.NAMESPACE_1);
        xmlStreamWriter.writeEndElement(); // End of <h:li>
        xmlStreamWriter.writeEndElement(); // End of <h:ul>

        xmlStreamWriter.setPrefix("t", StAXDemo.NAMESPACE_2);
        xmlStreamWriter.writeEndElement(); // Enf of <t:ingredients>
        xmlStreamWriter.writeEndElement(); // End of <t:recipe>
        xmlStreamWriter.writeEndElement(); // End of <h:body>
        xmlStreamWriter.writeEndElement(); // End of <h:html>

        // End of Document
        xmlStreamWriter.writeEndDocument();

        xmlStreamWriter.flush();
        xmlStreamWriter.close();
    }

    @Test
    @Order(1)
    @DisplayName("Test XML Stream Writer of StAX with FileWriter - ")
    void test_XML_Stream_Writer_With_File_Writer() {

        // Given
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();

        try (Writer fileWriter = new BufferedWriter(
                new FileWriter(outputFilePath.toFile()))) {

            IndentingXMLStreamWriter xmlStreamWriter =
                    new IndentingXMLStreamWriter(
                            xmlOutputFactory.createXMLStreamWriter(fileWriter));
            xmlStreamWriter.setIndentStep("    ");
            writeRecipeXml(xmlStreamWriter);

        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }

        // When & Then
        assertThat(Files.exists(outputFilePath))
                .as("recipe.xml should have been created.")
                .isTrue();
    }

    @Test
    @Order(3)
    @DisplayName("Test XML Stream Writer of StAX with StringWriter - ")
    void test_XML_Stream_Writer_With_String_Writer() {

        // Given
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();

        String recipeXml = null;

        try (StringWriter stringWriter = new StringWriter()) {

            XMLStreamWriter xmlStreamWriter =
                    xmlOutputFactory.createXMLStreamWriter(stringWriter);

            writeRecipeXml(xmlStreamWriter);
            stringWriter.flush();
            recipeXml = stringWriter.toString();

        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }

        // When & Then
        assertThat(recipeXml).isEqualTo(RECIPE_XML);
    }



}///:~