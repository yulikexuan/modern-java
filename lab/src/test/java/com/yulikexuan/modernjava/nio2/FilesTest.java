//: com.yulikexuan.modernjava.nio2.FilesTest.java


package com.yulikexuan.modernjava.nio2;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * String readString (Path path) throws IOException
 * String readString (Path path, Charset cs) throws IOException
 *     Reads all content from a file into a string,
 *     decoding from bytes to characters using the UTF-8 charset.
 *     The method ensures that the file is closed when all content have been
 *     read or an I/O error, or other runtime exception, is thrown.
 *     This method is equivalent to:
 *         readString(path, StandardCharsets.UTF_8)
 *
 * Path writeString (Path path, CharSequence csq, OpenOption... options)
 *         throws IOException
 * Path writeString (Path path, CharSequence csq, Charset cs, OpenOption... options)
 *         throws IOException
 *     Write a CharSequence to a file.
 *     Characters are encoded into bytes using the specified charset.
 *     All characters are written as they are, including the line separators in
 *     the char sequence. No extra characters are added.
 *     The options parameter specifies how the file is created or opened.
 *     If no options are present then this method works as if the CREATE,
 *     TRUNCATE_EXISTING, and WRITE options are present.
 *     In other words, it opens the file for writing, creating the file if it
 *     doesn't exist, or initially truncating an existing regular-file to a
 *     size of 0.
 *     Parameters:
 *         path - the path to the file
 *         csq - the CharSequence to be written
 *         cs - the charset to use for encoding
 *         options - options specifying how the file is opened
 *
 * long mismatch (Path path, Path path2) throws IOException
 *     Finds and returns the position of the first mismatched byte in the
 *     content of two files, or -1L if there is no mismatch.
 *     The position will be in the inclusive range of 0L up to the size (in bytes)
 *     of the smaller file.
 *     Two files are considered to match if they satisfy one of the following
 *     conditions:
 *         The two paths locate the same file, even if two equal paths locate a
 *         file does not exist, or
 *         The two files are the same size, and every byte in the first file is
 *         identical to the corresponding byte in the second file
 *     Otherwise there is a mismatch between the two files and the value
 *     returned by this method is:
 *         The position of the first mismatched byte, or
 *         The size of the smaller file (in bytes) when the files are different
 *         sizes and every byte of the smaller file is identical to the
 *         corresponding byte of the larger file
 *     This method may not be atomic with respect to other file system operations
 *     This method is always reflexive (for Path f, mismatch(f,f) returns -1L)
 *     If the file system and files remain static, then this method is symmetric
 *     (for two Paths f and g, mismatch(f,g) will return the same value as
 *     mismatch(g,f))
*
 */
@DisplayName("Files class's New Methods Test - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FilesTest {

    static final String DATA = RandomStringUtils.randomAlphanumeric(100);

    static final String FILE_NAME_1 = "test_file_1.data";
    static final String FILE_NAME_2 = "test_file_2.txt";

    private Path testFilePath_1;
    private Path testFilePath_2;

    @BeforeEach
    void setUp() throws IOException {
        this.testFilePath_1 = Paths.get(FILE_NAME_1).toAbsolutePath();
        this.testFilePath_2 = Paths.get(FILE_NAME_2).toAbsolutePath();
        Files.writeString(this.testFilePath_1, DATA);
        Files.writeString(this.testFilePath_2, DATA);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(this.testFilePath_1)) {
            Files.delete(this.testFilePath_1);
        }
        if (Files.exists(this.testFilePath_2)) {
            Files.delete(this.testFilePath_2);
        }
    }

    @Test
    void test_Given_A_File_Then_Read_String_From_It() throws IOException {

        // Given & When
        String data_1 = Files.readString(this.testFilePath_1);
        String data_2 = Files.readString(this.testFilePath_2);

        // Then
        assertThat(data_1).isEqualTo(DATA);
        assertThat(data_2).isEqualTo(data_1);
    }

    @Test
    void test_Given_Two_Files_Then_Knowing_If_They_Are_Match() throws IOException {

        // Given & When
        long mismatchPostion = Files.mismatch(this.testFilePath_1, this.testFilePath_2);

        // Then
        assertThat(mismatchPostion).isEqualTo(-1L);
    }

}///:~