//: com.yulikexuan.concurrency.testing.correctness.SemaphoreBoundedBufferCorrectnessTest.java

package com.yulikexuan.concurrency.testing.correctness;


import lombok.NonNull;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;


@DisplayName("Correctness Test of SemaphoreBoundedBuffer - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SemaphoreBoundedBufferCorrectnessTest {

    private static final int MIN_CAPACITY = 10;
    private static final int MAX_DATA_CAPACITY = 100_000;

    private static final long LOCKUP_DETECT_TIMEOUT = 300L;

    static class BigData {
        double[] data = new double[MAX_DATA_CAPACITY];
    }

    private SemaphoreBoundedBuffer<Integer> boundedBuffer;

    @BeforeEach
    void setUp() {
        this.boundedBuffer = SemaphoreBoundedBuffer.of(MIN_CAPACITY);
    }

    @Test
    void test_The_Buffer_Should_Be_Empty_When_Constructed() {

        // Then
        assertThat(this.boundedBuffer.isEmpty()).isTrue();
        assertThat(this.boundedBuffer.isFull()).isFalse();
    }

    private void fullBuffer() throws InterruptedException {
        for (int i = 0; i < MIN_CAPACITY; i++) {
            this.boundedBuffer.put(i);
        }
    }

    @Test
    void test_Being_Full_After_Put_All() throws InterruptedException {

        // Given
        this.fullBuffer();

        // When & Then
        assertThat(this.boundedBuffer.isFull()).isTrue();
        assertThat(this.boundedBuffer.isEmpty()).isFalse();
    }

    @Test
    void test_Take_Method_Blocks_When_Empty() {

        // Given
        final Thread taker = new Thread(() -> {
            try {
                int unused = boundedBuffer.take();
                fail(); // if we get here, it's an error
            } catch (InterruptedException success) {
                Thread.currentThread().interrupt();
            }
        });

        // When
        taker.start();

        // Then
        await().until(() -> !isThreadAlive(taker));
        assertThat(taker.isInterrupted()).isTrue();
    }

    @Test
    void test_Put_Method_Blocks_When_Full() throws InterruptedException {

        // Given
        this.fullBuffer();

        final Thread taker = new Thread(() -> {
            try {
                boundedBuffer.put(1);
                fail(); // if we get here, it's an error
            } catch (InterruptedException success) {
                Thread.currentThread().interrupt();
            }
        });

        // When
        taker.start();

        // Then
        await().until(() -> !isThreadAlive(taker));
        assertThat(taker.isInterrupted()).isTrue();
    }

    static boolean isThreadAlive(@NonNull Thread thread) {
        try {
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            thread.interrupt();
            thread.join(LOCKUP_DETECT_TIMEOUT);
            return thread.isAlive();
        } catch (Exception unexpected) {
            fail();
            throw new IllegalStateException();
        }
    }

}///:~