//: com.yulikexuan.modernjava.cache.GuavaCacheTest.java


package com.yulikexuan.modernjava.cache;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@Slf4j
@DisplayName("Guava Cache Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GuavaCacheTest {

    private static final int MAX_CACHE_SIZE = 3;

    private CacheLoader<String, String> upperCaseStringCacheLoader;
    private LoadingCache<String, String> upperCaseStringCache;

    @BeforeEach
    void setUp() {
        upperCaseStringCacheLoader =
                new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        return key.toUpperCase();
                    }
                };
    }

    @Test
    void test_When_Cache_Miss_Then_Value_Is_Computed() {

        // Given
        String testData = "Hello Guava Cache!";

        upperCaseStringCache = CacheBuilder.newBuilder()
                .build(upperCaseStringCacheLoader);

        long initCacheSize = upperCaseStringCache.size();

        // When
        String fromCache = upperCaseStringCache.getUnchecked(testData);

        // Then
        assertThat(initCacheSize).isEqualTo(0);
        assertThat(fromCache).isEqualTo(testData.toUpperCase());
        assertThat(upperCaseStringCache.size()).isEqualTo(1);
    }

    @Test
    public void test_When_Cache_Reach_Max_Size_Then_Eviction() {

        // Given
        upperCaseStringCache = CacheBuilder.newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .build(upperCaseStringCacheLoader);

        // When
        upperCaseStringCache.getUnchecked("first");
        upperCaseStringCache.getUnchecked("second");
        upperCaseStringCache.getUnchecked("third");
        upperCaseStringCache.getUnchecked("forth");

        // Then
        assertThat(upperCaseStringCache.size()).isEqualTo(MAX_CACHE_SIZE);
        assertThat(upperCaseStringCache.getIfPresent("first")).isNull();
        assertThat(upperCaseStringCache.getIfPresent("forth")).isEqualTo("FORTH");
    }

    @Test
    public void test_When_Cache_Reach_Max_Weight_Then_Eviction() {

        // Given
        upperCaseStringCache = CacheBuilder.newBuilder()
                .maximumWeight(16)
                .weigher((String key, String value) -> value.length())
                .build(upperCaseStringCacheLoader);

        // When
        upperCaseStringCache.getUnchecked("first");
        upperCaseStringCache.getUnchecked("second");
        upperCaseStringCache.getUnchecked("third");
        upperCaseStringCache.getUnchecked("last");

        // Then
        assertThat(upperCaseStringCache.size()).isEqualTo(MAX_CACHE_SIZE);
        assertThat(upperCaseStringCache.getIfPresent("first")).isNull();
        assertThat(upperCaseStringCache.getIfPresent("last")).isEqualTo("LAST");
    }

    @Test
    public void test_When_Entry_Idle_Then_Eviction() {

        // Given
        int availability = 100;
        upperCaseStringCache = CacheBuilder.newBuilder()
                .expireAfterAccess(availability, TimeUnit.MILLISECONDS)
                .build(upperCaseStringCacheLoader);

        upperCaseStringCache.getUnchecked("hello");
        assertThat(upperCaseStringCache.size()).isEqualTo(1);
        upperCaseStringCache.getUnchecked("hello");

        // When
        await().untilAsserted(() -> assertThat(
                upperCaseStringCache.getIfPresent("hello")).isNull());

        log.info(">>>>>> Size: {}", upperCaseStringCache.size());

        upperCaseStringCache.getUnchecked("test");

        // Then
        assertThat(upperCaseStringCache.size()).isEqualTo(1);
        assertThat(upperCaseStringCache.getIfPresent("hello")).isNull();
    }

    @Test
    void test_When_Entry_Live_Time_Expire_Then_Eviction() {

        // Given
        int availability = 200;
        upperCaseStringCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.of(availability, ChronoUnit.MILLIS))
                .build(upperCaseStringCacheLoader);

        upperCaseStringCache.getUnchecked("hello");
        assertThat(upperCaseStringCache.size()).isEqualTo(1);

        await().atMost(Duration.ofMillis(150)).untilAsserted(() ->
                assertThat(upperCaseStringCache).isNotNull());

        upperCaseStringCache.getUnchecked("hello");

        // When
        await().atLeast(Duration.ofMillis(60)).untilAsserted(() ->
                assertThat(upperCaseStringCache).isNotNull());

        upperCaseStringCache.getUnchecked("test");

        // Then
        assertThat(upperCaseStringCache.size()).isEqualTo(1);
        assertThat(upperCaseStringCache.getIfPresent("hello")).isNull();
    }

}///:~