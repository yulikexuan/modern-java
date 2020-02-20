//: com.yulikexuan.modernjava.vfs.ApacheVfsTest.java


package com.yulikexuan.modernjava.vfs;


import org.apache.commons.vfs2.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * All filenames are treated as URIs
 *
 * One of the consequences of this is you have to encode the '%' character
 * using %25
 * Depending on the filesystem additional characters are encoded if needed.
 * This is done automatically, but might be reflected in the filename.
 */
@Disabled
public class ApacheVfsTest {

    static final String THIS_FILE_URI =
            "file:///C:\\TecsysDev\\javaguru\\projects\\modern-java\\lab\\src" +
                    "\\test\\java\\com\\yulikexuan\\modernjava\\vfs";

    @BeforeEach
    void setUp() throws FileSystemException {
    }

    @Test
    void test_Paths_For_Prefix() throws FileSystemException {

        // Given
        String absPathStr = "/TecsysDev/javaguru/projects" +
                "/modern-java/lab/src/test/resources/fileTest.txt";

        Path path = Paths.get(absPathStr);

        URI uri = URI.create(absPathStr);
        System.out.println(uri);

        try (FileSystemManager defaultFileSystemManager = VFS.getManager();
             FileObject fileObject = defaultFileSystemManager
                     .resolveFile("file:///" + absPathStr);) {
            // When
//            fileObject.createFile();

            assertThat(fileObject.exists()).isFalse();
        }
    }

    @Test
    void test_If_Local_File_Exists() throws FileSystemException {

        // Given
        boolean exists = false;
        try (FileSystemManager defaultFileSystemManager = VFS.getManager();
                FileObject fileObject = defaultFileSystemManager.resolveFile(
                        THIS_FILE_URI)) {
            // When
            exists = fileObject.exists();
        }

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void test_Temp_Files() throws FileSystemException {

        // Given
        boolean exists = false;
        String tmpDir = System.getProperty("java.io.tmpdir");
        File file = new File(tmpDir);
        URI tmpUri = file.toURI();
        try (FileSystemManager defaultFileSystemManager = VFS.getManager();
             FileObject fileObject = defaultFileSystemManager.resolveFile(tmpUri)) {
            exists = fileObject.exists();
        }
        assertThat(exists).isTrue();
    }

    @Test
    void copy_InputStream_To_File() throws IOException {

        // Given
        Path inputFilePath = Paths.get("src", "test", "resources",
                "bootstrap.properties");
        Path outputFilePath = Paths.get("src", "test", "resources",
                "bootstrap_2.properties");

        // When
        try (FileSystemManager defaultFileSystemManager = VFS.getManager();
             FileObject inputFileObject = defaultFileSystemManager
                     .resolveFile(inputFilePath.toUri());
             FileObject outputFileObject = defaultFileSystemManager
                     .resolveFile(outputFilePath.toUri());
             InputStream inputStream = inputFileObject.getContent().getInputStream();) {

            outputFileObject.copyFrom(inputFileObject, Selectors.SELECT_SELF);
        }

    }

    @Test
    void test_File_Copy() throws URISyntaxException, FileSystemException {

        // Given
        Path inputFilePath = Paths.get("src", "test", "resources",
                "bootstrap.properties");
        Path outputFilePath = Paths.get("src", "test", "resources",
                "bootstrap_2.properties");

        try (FileSystemManager defaultFileSystemManager = VFS.getManager();
                FileObject inputFileObject = defaultFileSystemManager
                        .resolveFile(inputFilePath.toUri());
                FileObject outputFileObject = defaultFileSystemManager
                        .resolveFile(outputFilePath.toUri());) {

            outputFileObject.copyFrom(inputFileObject, Selectors.SELECT_SELF);
        }


    }

}///:~