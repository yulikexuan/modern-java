//: com.yulikexuan.concurrency.taskexe.parallelism.FutureRendererTest.java

package com.yulikexuan.concurrency.taskexe.parallelism;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;


@Disabled
@Slf4j
@DisplayName("Test FutureRenderer - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FutureRendererTest {

    private static final int IMAGE_COUNT = FutureRenderer.DEFAULT_IMAGE_COUNT;

    private StopWatch stopWatch;

    private FutureRenderer futureRenderer;

    @BeforeEach
    void setUp() throws Exception {
        this.futureRenderer = FutureRenderer.of();
        this.stopWatch = StopWatch.createStarted();
    }

    @Test
    void evaluate() {

        // Given
        this.futureRenderer.renderPage(RandomStringUtils.random(IMAGE_COUNT));

        // When
        this.stopWatch.stop();

        // Then
        long timeSpent = stopWatch.getTime(TimeUnit.MILLISECONDS);
        log.info(">>>>>>> Time spent for rendering {} images is {} ms",
                IMAGE_COUNT, timeSpent);
    }

}///:~