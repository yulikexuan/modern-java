//: com.yulikexuan.concurrency.taskexe.parallelism.CompletionServiceRendererTest.java

package com.yulikexuan.concurrency.taskexe.parallelism;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;


@Slf4j
@DisplayName("Test CompletionServiceRenderer - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CompletionServiceRendererTest {

    private static final int IMAGE_COUNT =
            CompletionServiceRenderer.DEFAULT_IMAGE_COUNT;

    private StopWatch stopWatch;

    private CompletionServiceRenderer renderer;

    @BeforeEach
    void setUp() throws Exception {
        this.renderer = CompletionServiceRenderer.of(
                CompletionServiceRenderer.DEFAULT_THREAD_POOL_SIZE);
        this.stopWatch = StopWatch.createStarted();
    }

    @Test
    void evaluate() {

        // Given
        this.renderer.renderPage(RandomStringUtils.random(IMAGE_COUNT));

        // When
        this.stopWatch.stop();

        // Then
        long timeSpent = stopWatch.getTime(TimeUnit.MILLISECONDS);
        log.info(">>>>>>> Time spent for rendering {} images is {} ms",
                IMAGE_COUNT, timeSpent);
    }

}///:~