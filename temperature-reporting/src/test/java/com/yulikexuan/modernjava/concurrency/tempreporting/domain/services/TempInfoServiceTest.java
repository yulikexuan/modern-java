//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.services.TempInfoServiceTest.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.services;


import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo;
import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.InvalidTemperatureException;

import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.TempInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TempInfoServiceTest {

    static final String TOWN = "Montréal";

    private CacheLoader<String, String> cacheLoader;
    private LoadingCache loadingCache;
    private TempInfoService tempInfoService;
    private String tempReport;

    @BeforeEach
    void setUp() {
        this.cacheLoader = new CacheLoader<String, String>() {
            public String load(String key) {
                return generateTempReport(key).toUpperCase();
            }
        };
        this.loadingCache = CacheBuilder.newBuilder().build(this.cacheLoader);
        this.tempInfoService = new TempInfoService();
    }

    @Test
    void test_Cache() throws ExecutionException {

        // Given
        long initCacheSize = this.loadingCache.size();

        // When
        tempReport = (String) this.loadingCache.getUnchecked(TOWN);
        long finalSize = this.loadingCache.size();
        System.out.println("From Guava: " + tempReport);

        // Then
        assertThat(initCacheSize).isEqualTo(0L);
        assertThat(tempReport).contains(TOWN.toUpperCase());
        assertThat(finalSize).isEqualTo(1L);
    }

    private String generateTempReport(String town) {
        Optional<ITempInfo> tempInfo = Optional.empty();
        try {
            tempInfo = Optional.of(this.tempInfoService.fetch(town));
        } catch (InvalidTemperatureException e) {
            System.out.println(e.getMessage());
        }

        return tempInfo.orElseGet(
                () -> TempInfo.builder().town(town).temp(20).build()).toString();
    }

}///:~