//: com.yulikexuan.concurrency.async.IImageService.java

package com.yulikexuan.concurrency.async;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public interface IImageService {
    CompletableFuture<Image> downloadImage(URI uri);
    void renderImage(Image image);
}


@AllArgsConstructor(staticName = "of")
class ImageService implements IImageService {

    private final ExecutorService executor;

    @Override
    public CompletableFuture<Image> downloadImage(@NonNull URI uri) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new NotFoundException(e);
            }
            return new Image(new byte[] {});
        }, executor);
    }

    @Override
    public void renderImage(@NonNull Image image) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new NotFoundException(e);
        }
    }

}//: End of class ImageService

@Value
class Image {
    byte[] data;
}

///:~