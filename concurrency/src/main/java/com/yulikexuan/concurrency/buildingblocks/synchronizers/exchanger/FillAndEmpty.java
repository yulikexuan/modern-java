//: com.yulikexuan.concurrency.buildingblocks.synchronizers.exchanger.FillAndEmpty.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers.exchanger;


import com.yulikexuan.concurrency.util.FileResourceUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Slf4j
class FillAndEmpty {

    private static final int BUFFER_SIZE = 1024;
    private static final String SRC_FILE_NAME = "logback-test.xml";
    private static final String TARGET_FILE_NAME_SUFFIX = "_bak";

    private final Exchanger<ByteBuffer> exchanger;

    private ByteBuffer initialEmptyBuffer;
    private ByteBuffer initialFullBuffer;

    public FillAndEmpty() throws IOException {
        this.exchanger = new Exchanger<>();
    }

    void start() {

        try {
            ReadableByteChannel srcChannel =
                    FileResourceUtil.getFileAsChannel(SRC_FILE_NAME);

            WritableByteChannel targetChannel =
                    FileResourceUtil.getWriteableChannel(
                            Files.createTempFile(SRC_FILE_NAME,
                                    TARGET_FILE_NAME_SUFFIX));

            this.initialEmptyBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            this.initialFullBuffer = ByteBuffer.allocate(BUFFER_SIZE);

            new Thread(new FillingLoop(srcChannel)).start();
            new Thread(new EmptyingLoop(targetChannel)).start();

        } catch (IOException ioe) {
            log.error(">>>>>>> The Exchanger failed: {}", ioe.getMessage());
        }
    }

    class FillingLoop implements Runnable {

        private final ReadableByteChannel srcChannel;

        FillingLoop(@NonNull ReadableByteChannel srcChannel) {
            this.srcChannel = srcChannel;
        }

        public void run() {

            ByteBuffer currentBuffer = initialEmptyBuffer;

            try {
                while (currentBuffer != null) {
                    int bytesRead = srcChannel.read(currentBuffer);
                    while (bytesRead != -1) {
                        log.info(">>>>>> Read {} bytes", bytesRead);
                        currentBuffer.flip();
                        currentBuffer = exchanger.exchange(currentBuffer,
                                1, TimeUnit.SECONDS);
                        bytesRead = srcChannel.read(currentBuffer);
                    }
                    currentBuffer.clear();
                    currentBuffer = null;
                }
            } catch (IOException | TimeoutException | InterruptedException x) {
                log.error(">>>>>> The filling job failed: {}", x.getMessage());
            } finally {
                try {
                    this.srcChannel.close();
                } catch (IOException ioe) {
                    log.error(">>>>>>> Closing source channel failed: {}",
                            ioe.getMessage());
                }
            }
        }
    }

    class EmptyingLoop implements Runnable {

        private final WritableByteChannel targetChannel;

        EmptyingLoop(WritableByteChannel targetChannel) {
            this.targetChannel = targetChannel;
        }

        @Override
        public void run() {

            ByteBuffer currentBuffer = initialFullBuffer;
            currentBuffer.put("".getBytes(StandardCharsets.UTF_8));
            currentBuffer.flip();

            try {
                while (currentBuffer != null) {
                    while(currentBuffer.hasRemaining()) {
                        targetChannel.write(currentBuffer);
                    }
                    currentBuffer.clear();
                    currentBuffer = exchanger.exchange(currentBuffer,
                            1, TimeUnit.SECONDS);
                }
            } catch (IOException | TimeoutException | InterruptedException x)  {
                log.error(x.getMessage());
            } finally {
                try {
                    this.targetChannel.close();
                } catch (IOException ioe) {
                    log.error(">>>>>>> Closing target channel failed: {}",
                            ioe.getMessage());
                }
            }
        }
    }

}///:~