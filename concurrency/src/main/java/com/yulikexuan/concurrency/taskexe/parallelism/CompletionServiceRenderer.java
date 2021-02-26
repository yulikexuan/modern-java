//: com.yulikexuan.concurrency.taskexe.parallelism.CompletionServiceRenderer.java

package com.yulikexuan.concurrency.taskexe.parallelism;


import com.google.common.collect.Lists;
import com.yulikexuan.concurrency.buildingblocks.synchronizers.LaunderThrowable;
import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;


/**
 * Renderer
 * <p/>
 * Using CompletionService to render page elements as they become available
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
public class CompletionServiceRenderer {

    static final int DEFAULT_THREAD_POOL_SIZE = 16;
    static final int DEFAULT_IMAGE_COUNT = 4;
    static final int TEXT_RENDERING_MILLIS = 200;
    static final int RENDERING_EACH_IMAGE_MILLIS = 10;

    private final ExecutorService executor;

    private CompletionServiceRenderer(@NonNull ExecutorService executor) {
        this.executor = executor;
    }

    public static CompletionServiceRenderer of(
            int threadPoolSize) throws Exception {

        threadPoolSize = ((threadPoolSize <= 0) ||
                (threadPoolSize > DEFAULT_THREAD_POOL_SIZE)) ?
                DEFAULT_THREAD_POOL_SIZE : threadPoolSize;

        return new CompletionServiceRenderer(ExecutorServiceFactory
                .createFixedPoolSizeExecutor(DEFAULT_THREAD_POOL_SIZE));
    }

    void renderPage(CharSequence source) {

        final List<IImageInfo> info = scanForImageInfo(source);

        CompletionService<IImageData> completionService =
                new ExecutorCompletionService<>(executor);

        for (final IImageInfo imageInfo : info) {
            completionService.submit(() -> imageInfo.downloadImage());
        }

        renderText(source);

        try {
            for (int t = 0, n = info.size(); t < n; t++) {
                Future<IImageData> f = completionService.take();
                IImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }

    private List<IImageInfo> scanForImageInfo(CharSequence s) {

        List<IImageInfo> imageInfoList = Lists.newArrayList();

        for (int i = 0; i < s.length(); i++) {
            imageInfoList.add(new ImageInfo());
        }

        return imageInfoList;
    }

    private void renderText(@NonNull CharSequence s) {
        try {
            TimeUnit.MILLISECONDS.sleep(TEXT_RENDERING_MILLIS);
        } catch (InterruptedException e) {
            log.error(">>>>>>> Rendering text was interrupted.");
        }
    }

    private void renderImage(IImageData i) {
        try {
            TimeUnit.MILLISECONDS.sleep(RENDERING_EACH_IMAGE_MILLIS);
        } catch (InterruptedException e) {
            log.error(">>>>>>> Rendering images was interrupted.");
        }
    }

}///:~