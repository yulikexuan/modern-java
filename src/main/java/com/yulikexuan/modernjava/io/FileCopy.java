//: com.yulikexuan.modernjava.io.FileCopy.java

package com.yulikexuan.modernjava.io;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;


public class FileCopy {

    URL getResource(String resourceName) {
        return this.getClass().getClassLoader().getResource(resourceName);
    }

    String getResourcePath(String resourceName) {
        return this.getResource(resourceName).getPath();
    }

    File getFile(String fileName) {
        return FileUtils.getFile(this.getResourcePath(fileName));
    }

    Optional<String> getFileContent(File file) throws IOException {
        return file == null ? Optional.empty() :
                Optional.ofNullable(FileUtils.readFileToString(
                        file, Charset.defaultCharset()));
    }

}///:~