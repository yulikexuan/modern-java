//: com.yulikexuan.modernjava.collections.concurrent.ConcurrentHashMapTest.java


package com.yulikexuan.modernjava.collections.concurrent;


import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


public class ConcurrentHashMapTest {

    private MessageDigest messageDigest;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("SHA-256");
    }

    @Nested
    @DisplayName("Thread Safety Test for Different Maps - ")
    class ThreadSafetyTestForMaps {

        private List<Integer> parallelSum100(
                Map<String, Integer> map, int executionTimes)
                throws InterruptedException {

            List<Integer> sumList = new ArrayList<>(1000);

            for (int i = 0; i < executionTimes; i++) {
                map.put("test", 0);
                ExecutorService executorService = Executors.newFixedThreadPool(
                        4);
                for (int j = 0; j < 10; j++) {
                    executorService.execute(
                            () -> {
                                for (int k = 0; k < 10; k++) {
                                    map.computeIfPresent("test",
                                            (key, value) -> ++value);
                                }
                            }
                    );
                }
                executorService.shutdown();
                executorService.awaitTermination(5, TimeUnit.SECONDS);
                sumList.add(map.get("test"));
            }
            return sumList;
        }

        private BiConsumer<Long, Long> threadSafetyAssertionForHashMap =
                (distinctResultCount, wrongResultCount) -> assertAll(
                        () -> assertThat(distinctResultCount).isGreaterThan(1),
                        () -> assertThat(wrongResultCount).isGreaterThan(0));

        private BiConsumer<Long, Long> threadSafetyAssertionForConcurrentHashMap =
                (distinctResultCount, wrongResultCount) -> assertAll(
                        () -> assertThat(distinctResultCount).isEqualTo(1),
                        () -> assertThat(wrongResultCount).isEqualTo(0));

        private void test_Thread_Safety(Map<String, Integer> map,
                                        BiConsumer<Long, Long> assertion)
                throws InterruptedException {

            // Given
            List<Integer> sumList = parallelSum100(map, 100);

            // When
            long count = sumList.stream()
                    .distinct()
                    .count();

            long wrongResultCount = sumList.stream()
                    .filter(num -> num != 100)
                    .count();

            // Then
            assertion.accept(count, wrongResultCount);
        }

        @Test
        void test_Thread_Safety_HashMap() throws InterruptedException {
            test_Thread_Safety(Maps.newHashMap(),
                    this.threadSafetyAssertionForHashMap);
        }

        @Test
        void test_Thread_Safety_ConcurrentHashMap() throws InterruptedException {
            test_Thread_Safety(new ConcurrentHashMap<>(),
                    this.threadSafetyAssertionForConcurrentHashMap);
        }

    } //: End of class ThreadSafetyTestForMaps

    @Nested
    @DisplayName("Null Key / Value Test - ")
    class NullKeyValueTest {

        private Map<String, byte[]> secretCodes;

        private String key;
        private byte[] value;

        @BeforeEach
        void setUp() {
            this.secretCodes = new ConcurrentHashMap<>();
        }

        @Test
        @DisplayName("ConcurrentHashMap does not accept null key - ")
        void test_If_Accepting_Null_Key() {

            // Given
            value = messageDigest.digest(
                    RandomStringUtils.randomAlphanumeric(10).getBytes(
                            StandardCharsets.UTF_8));

            // When & Then
            assertThatThrownBy(() -> this.secretCodes.put(key, value))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("ConcurrentHashMap does not accept null value - ")
        void test_If_Accepting_Null_Value() {

            // Given
            key = RandomStringUtils.randomAlphanumeric(10);

            // When & Then
            assertThatThrownBy(() -> this.secretCodes.put(key, value))
                    .isInstanceOf(NullPointerException.class);
        }

    } //: End of class NullKeyValueTest

    @Nested
    @DisplayName("Parallelism Threshold Test - ")
    class ParallelismThresholdTest {

        private List<String> lines;
        private ConcurrentHashMap<String, byte[]> secretCodes;

        @BeforeEach
        void setUp() {
            lines = IntStream.range(0, 10)
                    .mapToObj(i -> RandomStringUtils.randomAlphanumeric(10))
                    .collect(Collectors.toList());

            secretCodes = new ConcurrentHashMap<>();

            lines.stream()
                    .forEach(k -> secretCodes.computeIfAbsent(k,
                            key -> messageDigest.digest(
                                    key.getBytes(StandardCharsets.UTF_8))));
        }

        @Test
        void test_For_Each_Method() {
            this.secretCodes.forEach(10,
                    (k, v) -> System.out.printf(
                            "%1$s is related with %2$s by thread %3$s%n",
                            k,
                            Hex.encodeHexString(v),
                            Thread.currentThread().getName()));
        }

    } //: End of class ParallelismThresholdTest

}///:~