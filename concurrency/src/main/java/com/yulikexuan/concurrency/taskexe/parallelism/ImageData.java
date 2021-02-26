//: com.yulikexuan.concurrency.taskexe.parallelism.ImageData.java

package com.yulikexuan.concurrency.taskexe.parallelism;


import lombok.AllArgsConstructor;

import java.util.Arrays;


@AllArgsConstructor(staticName = "of")
public final class ImageData implements IImageData {

    private final byte[] bytes;

    public byte[] getBytes() {
        return Arrays.copyOf(this.bytes, this.bytes.length);
    }

}///:~