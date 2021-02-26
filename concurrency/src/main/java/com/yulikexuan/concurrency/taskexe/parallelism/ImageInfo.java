//: com.yulikexuan.concurrency.taskexe.parallelism.ImageInfo.java

package com.yulikexuan.concurrency.taskexe.parallelism;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


@Slf4j
public class ImageInfo implements IImageInfo {

    @Override
    public IImageData downloadImage() {

        try {
            TimeUnit.MILLISECONDS.sleep(
                    FutureRenderer.DOWNLOADING_EACH_IMAGE_MILLIS);
        } catch (InterruptedException e) {
            log.error(">>>>>>> Image data downloading was inturrupted.");
        }

        return ImageData.of(RandomStringUtils.randomAlphanumeric(17)
                .getBytes(StandardCharsets.UTF_8));
    }

}///:~