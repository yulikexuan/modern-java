//: com.yulikexuan.modernjava.stax.StAXDemoTest.java


package com.yulikexuan.modernjava.stax;


import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


class StAXDemoTest {

    private static XMLInputFactory xmlInputFactory;
    private static boolean inputFileExists = false;
    private static Path inputFilePath;

    @BeforeAll
    static void beforeAll() throws URISyntaxException {
        xmlInputFactory = XMLInputFactory.newFactory();
        inputFilePath = StAXDemo.getInputFilePath();
        inputFileExists = Files.exists(inputFilePath);
    }

    @BeforeEach
    void setUp() throws URISyntaxException {
    }

    @Test
    @DisplayName("Test XML Stream Reader of StAX - ")
    void test_Xml_Stream_Reader() {

        assumeTrue(inputFileExists);

        // Given
        XMLStreamReader xmlStreamReader = null;
        StringBuilder xmlOutputBuilder = new StringBuilder();
        try (FileReader fileReader = new FileReader(inputFilePath.toFile())) {
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
        int appearanceRoot = StringUtils.countMatches(outputXml,
                "Qname = breakfast_menu");
        int appearanceFood = StringUtils.countMatches(outputXml,"Qname = food");
        int appearanceName = StringUtils.countMatches(outputXml,"Qname = name");

        // Then
        assertThat(appearanceRoot).isEqualTo(2);
        assertThat(appearanceFood).isEqualTo(2 * StAXDemo.NUMBER_OF_EMEMENTS);
        assertThat(appearanceName).isEqualTo(2 * StAXDemo.NUMBER_OF_EMEMENTS);
    }

    @Test
    @DisplayName("Test XML Stream Writer of StAX - ")
    void test_XML_Stream_Writer() {

    }

}///:~