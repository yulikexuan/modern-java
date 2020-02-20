//: com.yulikexuan.modernjava.nio.FileChannelTest.java


package com.yulikexuan.modernjava.nio;


import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class FileChannelTest {

    static final Path TEST_FILE_PATH = Paths.get(
            "src/test/resources/NioTest.txt");

    static final Path TEST_FILE_COPY_PATH = Paths.get(
            "src/test/resources/NioCopyTest.txt");

    private String fileContent;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        this.fileContent = Files.lines(TEST_FILE_PATH)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Test
    @DisplayName("Test file channel for reading - ")
    void test_Reading_File_With_FileChannel() throws IOException {

        // Given
        String actualFileContent = "";
        long finalChannelPosition = -1;
        long fileSize = -1;

        // When
        //  Closing a RandomAccessFile will also close the associated channel
        try (RandomAccessFile fileReader =
                     new RandomAccessFile(TEST_FILE_PATH.toFile(), "r");
             FileChannel fileReaderChannel = fileReader.getChannel();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {

            fileReaderChannel.force(true);

            int bufferSize = 1024;
            // The file size of the FileChannel
            long channelSize = fileReaderChannel.size();
            bufferSize = (bufferSize > channelSize) ?
                    (int)channelSize : bufferSize;

            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

            while (fileReaderChannel.read(byteBuffer) > 0) {
                outputStream.write(byteBuffer.array(), 0,
                        byteBuffer.position());
                // Or, outputStream.writeBytes(byteBuffer.array());
                byteBuffer.clear();
            }

            byte[] contentArray = outputStream.toByteArray();
            actualFileContent = new String(contentArray, StandardCharsets.UTF_8);
            finalChannelPosition = fileReaderChannel.position();
            fileSize = fileReaderChannel.size();
        }

        // Then
        assertThat(actualFileContent).isEqualTo(this.fileContent);
        assertThat(finalChannelPosition).isEqualTo(fileSize);
    }

    @Test
    @DisplayName("Test file channel for writing - ")
    void test_Writing_File_With_FileChannel() throws IOException {

        // Given
        if (!Files.exists(TEST_FILE_COPY_PATH)) {
            Files.createFile(TEST_FILE_COPY_PATH);
        }

        // When
        try (RandomAccessFile fileCopy = new RandomAccessFile(
                    TEST_FILE_COPY_PATH.toFile(), "rw");
                FileChannel channel = fileCopy.getChannel();) {

            channel.force(true);

            ByteBuffer buffer = ByteBuffer.wrap(this.fileContent.getBytes(
                    StandardCharsets.UTF_8));

            channel.write(buffer);
        }

        String fileCopyContent = Files.lines(TEST_FILE_COPY_PATH)
                .collect(Collectors.joining(System.lineSeparator()));

        // Assert
        assertThat(fileCopyContent).isEqualTo(this.fileContent);
    }

    @Test
    @DisplayName("Test loading a section of a file into memory - ")
    void test_Loading_A_Section_Of_A_File_Into_Memory() throws IOException {

        // Given

        // The buffer and the mapping that it represents will remain valid
        // until the buffer itself is garbage-collected.
        MappedByteBuffer mappedByteBuffer = null;
        byte[] data = null;

        // When
        try (FileChannel readerChannel = new RandomAccessFile(
                        TEST_FILE_PATH.toFile(), "r").getChannel();
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()) {

            mappedByteBuffer = readerChannel.map(
                    FileChannel.MapMode.READ_ONLY, 10, 9);
        }

        // Buffer::hasRemaining Tells whether there are any elements between
        // the current position and the limit
        // Buffer::remaining Returns the number of elements between the current
        // position and the limit.
        if (mappedByteBuffer != null && mappedByteBuffer.hasRemaining()) {
            data = new byte[mappedByteBuffer.remaining()];
            mappedByteBuffer.get(data);
        }

        // Then
        assertThat(new String(data, StandardCharsets.UTF_8))
                .isEqualTo("brown fox");
    }

}///:~