//: com.yulikexuan.concurrency.util.FileResourceUtil.java

package com.yulikexuan.concurrency.util;


import lombok.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;


public class FileResourceUtil {

    public static ReadableByteChannel getFileAsChannel(
            @NonNull String fileName) throws IOException {

        ClassLoader classloader =
                Thread.currentThread().getContextClassLoader();

        URL fileUrl = classloader.getResource(fileName);

        return Channels.newChannel(fileUrl.openStream());
    }

    public static WritableByteChannel getWriteableChannel(@NonNull Path path)
            throws IOException {

        return Channels.newChannel(new FileOutputStream(path.toFile()));
    }

}///:~