//: com.yulikexuan.concurrency.testing.correctness.ResourceManagementTest.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


@Disabled
@Slf4j
@DisplayName("Test Resource Management of SemaphoreBoundedBuffer- ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResourceManagementTest {

    private static final int DEFAULT_CAPACITY = 1000;

    private SemaphoreBoundedBuffer<BigData> bb;

    @BeforeEach
    void setUp() {
        this.bb = SemaphoreBoundedBuffer.of(DEFAULT_CAPACITY);
    }

    @Test
    void given_BoundedBuffer_When_Fulling_() throws Exception {

        // Given
        // printMemoryUsage();

        long freeMemo = Runtime.getRuntime().freeMemory();
        log.info(">>>>>>> #1 Free Memory [{}]", freeMemo);

        long totalMemo = Runtime.getRuntime().totalMemory();
        log.info(">>>>>>> #1 Total Memory [{}]", totalMemo);

        long maxMemo = Runtime.getRuntime().maxMemory();
        log.info(">>>>>>> #1 Max Memory [{}]", maxMemo);

        long snapshot_1 = totalMemo - freeMemo;
        log.info(">>>>>>> #1 Heap Snapshot [{}]", snapshot_1);

        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            bb.put(BigData.of());
        }

        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            bb.take();
        }

        System.gc();

        Thread.sleep(2000);

        freeMemo = Runtime.getRuntime().freeMemory();
        log.info(">>>>>>> #2 Free Memory [{}]", freeMemo);

        totalMemo = Runtime.getRuntime().totalMemory();
        log.info(">>>>>>> #2 Total Memory [{}]", totalMemo);

        maxMemo = Runtime.getRuntime().maxMemory();
        log.info(">>>>>>> #2 Max Memory [{}]", maxMemo);

        long snapshot_2 = totalMemo - freeMemo;
        log.info(">>>>>>> #2 Heap Snapshot [{}]", snapshot_2);

        long leak = snapshot_1 - snapshot_2;
        log.info(">>>>>>> The leak is [{}]", leak);

        // int heapSize2 = /* snapshot heap */;
        // assertTrue(Math.abs(heapSize1-heapSize2) < THRESHOLD);
    }

}///:~