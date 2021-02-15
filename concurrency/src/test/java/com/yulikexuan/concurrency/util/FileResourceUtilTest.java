//: com.yulikexuan.concurrency.util.FileResourceUtilTest.java

package com.yulikexuan.concurrency.util;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;


@Slf4j
@DisplayName("Test FileResourceUtil - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileResourceUtilTest {

    private static final int BUFFER_SIZE = 16;
    private static final String FILE_NAME = "logback-test.xml";

    @BeforeEach
    void setUp() {
    }


    @Test
    @Disabled
    void test_Being_Able_To_Read_A_File_From_Resource_With_Channel() {

        // Given
        try (ReadableByteChannel channel = FileResourceUtil.getFileAsChannel(
                FILE_NAME);) {

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            int bytesRead = channel.read(buffer);
            while (bytesRead != -1) {
                log.info(">>>>>> Read {} bytes", bytesRead);
                buffer.flip();
                while(buffer.hasRemaining()) {
                    log.info(StandardCharsets.UTF_8.decode(buffer).toString());
                }
                buffer.clear();
                bytesRead = channel.read(buffer);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }

}///:~