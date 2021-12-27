//: com.yulikexuan.jdk.ocp2.nio.DirectoryStreamUsage.java

package com.yulikexuan.jdk.ocp2.nio;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Slf4j
final class DirectoryStreamUsage {

    public static void main(String[] args) throws IOException {
        Path dir = Paths.get("c:/temp/x");
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(dir, "*/*.txt")) {

            for (Path path : stream) {
                log.info(">>>>>>> Path: {}", path);
            }
        }
    }

}///:~