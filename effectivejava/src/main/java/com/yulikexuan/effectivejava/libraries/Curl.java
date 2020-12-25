//: com.yulikexuan.effectivejava.libraries.Curl.java

package com.yulikexuan.effectivejava.libraries;


import com.google.common.base.Charsets;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


@Slf4j
class Curl {

    public static void main(String[] args) throws IOException {
        try (InputStream in = new URL(args[0]).openStream()) {
            in.transferTo(System.out);
        }
    }

    static String printContentOfUrl(@NonNull String url) throws IOException {
        try (InputStream in = new URL(url).openStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            in.transferTo(out);
            return IOUtils.toString(out.toByteArray(), Charsets.UTF_8.name());
        }
    }

}///:~