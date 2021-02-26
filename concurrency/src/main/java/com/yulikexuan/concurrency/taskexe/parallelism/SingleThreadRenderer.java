//: com.yulikexuan.concurrency.taskexe.parallelism.SingleThreadRenderer.java

package com.yulikexuan.concurrency.taskexe.parallelism;


import com.google.common.collect.Lists;

import java.util.List;

/**
 * SingleThreadRendere
 * <p/>
 * Rendering page elements sequentially
 *
 * @author Brian Goetz and Tim Peierls
 */
public abstract class SingleThreadRenderer {

    void renderPage(CharSequence source) {

        renderText(source);

        final List<ImageData> imageData = Lists.newArrayList();

        /*
         * Rendering the text elements first, leaving rectangular placeholders
         * for the images
         */
        for (ImageInfo imageInfo : scanForImageInfo(source)) {
            imageData.add(imageInfo.downloadImage());
        }

        /*
         * Downloading the images and drawing them into the associated placeholder
         */
        for (ImageData data : imageData) {
            renderImage(data);
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);

}///:~