//: com.yulikexuan.concurrency.taskexe.parallelism.FutureRenderer.java

package com.yulikexuan.concurrency.taskexe.parallelism;


import com.google.common.collect.Lists;
import com.yulikexuan.concurrency.buildingblocks.synchronizers.LaunderThrowable;
import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * FutureRenderer
 * <p/>
 * Waiting for image download with \Future
 *
 * The real performance payoff of dividing a program’s workload into tasks comes
 * when there are a large number of independent, homogeneous tasks that can be
 * processed concurrently
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
class FutureRenderer {

    static final int DEFAULT_IMAGE_COUNT = 4;
    static final int TEXT_RENDERING_MILLIS = 200;
    static final int RENDERING_EACH_IMAGE_MILLIS = 10;
    static final int DOWNLOADING_EACH_IMAGE_MILLIS = 1000;


    private final ExecutorService executor;

    FutureRenderer(@NonNull ExecutorService executor) {
        this.executor = executor;
    }

    public static FutureRenderer of() throws Exception {
        return new FutureRenderer(ExecutorServiceFactory
                .createFixedPoolSizeExecutor(DEFAULT_IMAGE_COUNT));
    }

    void renderPage(CharSequence source) {

        final List<IImageInfo> imageInfos = scanForImageInfo(source);

        /*
         * However, assigning a different type of task to each worker does not
         * scale well; if several more people show up, it is not obvious how
         * they can help without getting in the way or significantly
         * restructuring the division of labor
         *
         * Without finding finer-grained parallelism among similar tasks,
         * this approach will yield diminishing returns
         *
         * A further problem with dividing heterogeneous tasks among multiple
         * workers is that the tasks may have disparate sizes
         *
         * If you divide tasks A and B between two workers but A takes ten
         * times as long as B, you’ve only speeded up the total process by 9%
         *
         * Finally, dividing a task among multiple workers always involves some
         * amount of coordination overhead; for the division to be worthwhile,
         * this overhead must be more than compensated by productivity
         * improvements due to parallelism
         *
         * If rendering the text is much faster than downloading the images,
         * as is entirely possible, the resulting performance is not much
         * different from the sequential version, but the code is a lot more
         * complicated.
         *
         * And the best we can do with two threads is speed things up by a
         * factor of two.
         *
         * Thus, trying to increase concurrency by parallelizing heterogeneous
         * activities can be a lot of work, and there is a limit to how much
         * additional concurrency you can get out of it
         */
        Callable<List<IImageData>> task = new Callable<>() {
            public List<IImageData> call() {
                List<IImageData> result = new ArrayList<IImageData>();
                for (IImageInfo imageInfo : imageInfos) {
                    result.add(imageInfo.downloadImage());
                }
                return result;
            }
        };

        // Need at least: 2000 ms (for 20 images)
        Future<List<IImageData>> future = executor.submit(task);

        // Need at least: 200 ms
        renderText(source);

        try {
            List<IImageData> imageData = future.get();

            // Need at least: 200 ms (for 20 images)
            for (IImageData data : imageData) {
                renderImage(data);
            }

        } catch (InterruptedException e) {
            // Re-assert the thread's interrupted status
            Thread.currentThread().interrupt();
            // We don't need the result, so cancel the task too
            future.cancel(true);
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