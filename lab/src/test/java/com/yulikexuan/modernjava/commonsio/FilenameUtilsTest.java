//: com.yulikexuan.modernjava.commonsio.FilenameUtilsTest.java


package com.yulikexuan.modernjava.commonsio;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.net.URI;

public class FilenameUtilsTest {

    @Test
    void test_Getting_Full_Path() {

        // Given
        String path = "file:///c:\\temp\\ice\\info*";

        // When
        String parent = FilenameUtils.separatorsToUnix(
                FilenameUtils.getFullPathNoEndSeparator(path));
        String filename = FilenameUtils.getName(path);

        // Given
        System.out.println(parent);
        System.out.println(filename);
        System.out.println(">>>>>>> Has extension? " +
                FilenameUtils.getExtension(filename));
    }

    @Test
    void test_Uri() {

        // Given
        URI uri = URI.create("file:///C:/home/temp/info.txt");

        // When
        System.out.println(FilenameUtils.getFullPath(uri.getPath().toString()));

    }
}///:~