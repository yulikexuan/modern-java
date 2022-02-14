//: com.yulikexuan.modernjava.cache.caffeine.DataObjectCacheTest.java

package com.yulikexuan.modernjava.cache.caffeine;


import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Caffeine Cache of DataObject - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DataObjectCacheTest {

    private Cache<String, DataObject> cache;

    @BeforeEach
    void setUp() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(100)
                .build();

    }

    @Test
    void null_Value_For_Not_Present_Key() {

        // Given
        String key = "A";

        // When
        DataObject dataObj = this.cache.getIfPresent(key);

        // Then
        assertThat(dataObj).isNull();
    }

    @Test
    void able_To_Populate_Key_Value_Manually() {

        // Given
        String key = "A";
        DataObject dataObject = DataObject.of(key.toLowerCase(Locale.ROOT));

        //When
        cache.put(key, dataObject);

        // Then
        assertThat(cache.getIfPresent(key)).isSameAs(dataObject);
    }

    @Nested
    @DisplayName("Test Caffeine Synchromous Loading Cache - ")
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class SynchromousLoadingTest {

        @Test
        void able_To_Populate_Key_Value_With_Mapping_Function() {

            // Given
            String key = "A";
            DataObject dataObject = DataObject.of(key.toLowerCase(Locale.ROOT));

            //When
            // cache.put(key, dataObject);
            DataObject cachedData = cache.get(key, k -> dataObject);

            // Then
            assertThat(cachedData).isSameAs(dataObject);
        }

        @Test
        void able_To_Load_Synchronously() {

            // Given
            String key = "A";

            LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(Duration.ofMinutes(1))
                    .build(k -> DataObject.of("Data for " + k));

            // When
            DataObject dataObject = cache.get(key);
            Map<String, DataObject> cachedValues = cache.getAll(List.of(
                    "B", "C", "D"));

            // Then
            assertThat(dataObject.getData()).isEqualTo("Data for A");
            assertThat(cachedValues)
                    .hasEntrySatisfying("B",dataObj -> dataObj.getData()
                            .equals("Data for B"))
                    .hasEntrySatisfying("C", dataObj -> dataObj.getData()
                            .equals("Data for C"))
                    .hasEntrySatisfying("D", dataObj -> dataObj.getData()
                            .equals("Data for D"));
        }

    }//: End of class SynchronousLoadingTest

    @Nested
    @DisplayName("Test Caffeine Asynchromous Loading Cache - ")
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class AsynchromousLoadingTest {

        private AsyncLoadingCache<String, DataObject> asynchLoadingCache;

        @BeforeEach
        void setUp() {
            this.asynchLoadingCache = Caffeine.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(Duration.ofMinutes(1))
                    .buildAsync(k -> DataObject.of("Data for " + k));
        }

        @Test
        void able_To_Consume_Cached_Value_Asynchronously() {

            // Given
            String key = "A";

            // When
            this.asynchLoadingCache.get(key).thenAccept(
                    objData -> assertThat(objData).isEqualTo("Data for A"));
        }

    }

}///:~