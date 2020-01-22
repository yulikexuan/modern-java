//: com.yulikexuan.modernjava.stax.StAXDemo.java


package com.yulikexuan.modernjava.stax;


import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StAXDemo {

    public static final String XML_FILE_NAME = "/Food_Menu.xml";
    public static final int NUMBER_OF_EMEMENTS = 3;

    public static Path getInputFilePath() throws URISyntaxException {
        return Paths.get(StAXDemo.class
                .getResource(StAXDemo.XML_FILE_NAME).toURI());
    }

    public static boolean isInputFileExists() throws URISyntaxException {
        return Files.exists(getInputFilePath());
    }

    public static void main(String[] args) throws URISyntaxException {
        System.out.println(isInputFileExists());
    }

}///:~