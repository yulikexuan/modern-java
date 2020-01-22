//: com.yulikexuan.modernjava.stax.StAXDemo.java


package com.yulikexuan.modernjava.stax;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StAXDemo {

    public static final String XML_OUTPUT_FILE_NAME = "/recipe.xml";

    public static final int NUMBER_OF_EMEMENTS = 3;

    public static final String NAMESPACE_1 = "http://www.w3.org/1999/xhtml";
    public static final String NAMESPACE_2 = "http://www.tecsys.com";

    public static Path getOutputFilePath() throws URISyntaxException,
            IOException {

        return Paths.get("src", "main", "resources", "recipe.xml");
    }

}///:~