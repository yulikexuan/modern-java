//: com.yulikexuan.modernjava.collections.concurrent.ConcurrentHashMapTest.java


package com.yulikexuan.modernjava.collections.concurrent;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.*;
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
    @DisplayName("ConcurrentHashMap::forEach with Parallelism Threshold Test - ")
    class ParallelismThresholdForEachTest {

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

        @DisplayName("Test parallel forEach - ")
        @Test
        void test_Parallel_ForEach_Method() {

            // Given
            List<String> threadNames = Lists.newArrayList();

            this.secretCodes.forEach(5,
                    (k, v) ->  threadNames.add(Thread.currentThread().getName()));

            // When
            long threadNumber = threadNames.stream().distinct().count();

            // Then
            assertThat(threadNumber)
                    .as("Should having more than one thread for " +
                            "processing each element")
                    .isGreaterThan(1);
        }

        @DisplayName("Test single thead forEach - ")
        @Test
        void test_Single_Thread_ForEach_Method() {

            // Given
            List<String> threadNames = Lists.newArrayList();

            this.secretCodes.forEach(Long.MAX_VALUE,
                    (k, v) ->  threadNames.add(Thread.currentThread().getName()));

            // When
            long threadNumber = threadNames.stream().distinct().count();

            // Then
            assertThat(threadNumber).as("Should having only one " +
                            "thread for processing each element")
                    .isEqualTo(1L);
        }

        @DisplayName("Test Map::forEach method with BiFunction Parameter - ")
        @Test
        void test_ForEach_Transformation_Method() {

            // Given
            List<String> hexSecretCodes = Lists.newArrayList();

            this.secretCodes.forEach(
                    Long.MAX_VALUE,
                    (k, v) -> "0x" + Hex.encodeHexString(v),
                    v -> hexSecretCodes.add(v));

            // When
            int codeCount = hexSecretCodes.size();
            long resultCount = hexSecretCodes.stream()
                    .map(hexCode -> NumberUtils.isCreatable(hexCode))
                    .filter(result -> result == true)
                    .distinct()
                    .count();

            // Then
            assertThat(codeCount).isEqualTo(this.secretCodes.size());
            assertThat(resultCount)
                    .as("All hex secret codes shoulg be HEX creatable")
                    .isEqualTo(1);
        }

    } //: End of class ParallelismThresholdTest

    @Nested
    @DisplayName("ConcurrentHashMap::reduce Test - ")
    class ConcurrentHashMapReduceTest {

        private ConcurrentHashMap<String, Integer> yearlyGrossProfits;
        private ThreadLocalRandom random;

        private int getRandomProfit() {
            return random.nextInt(10_000, 100_000);
        }

        @BeforeEach
        void setUp() {
            random = ThreadLocalRandom.current();
            yearlyGrossProfits = new ConcurrentHashMap<>();
            yearlyGrossProfits.put("2019", getRandomProfit());
            yearlyGrossProfits.put("2018", getRandomProfit());
            yearlyGrossProfits.put("2017", getRandomProfit());
            yearlyGrossProfits.put("2016", getRandomProfit());
            yearlyGrossProfits.put("2015", getRandomProfit());
        }

        @Test
        @DisplayName("Test ConcurrentHashMap's reduce key and value method - ")
        void test_Reduce_Key_And_Value() {

            // Given

            // When
            int totalGrossProfit = this.yearlyGrossProfits.reduceValues(
                    Long.MAX_VALUE, Integer::sum);
            int totalNetProfit = this.yearlyGrossProfits.reduce(Long.MAX_VALUE,
                    (k, v) -> v / 2, Integer::sum);

            // Then
            assertThat(totalNetProfit).isCloseTo(totalGrossProfit / 2,
                    Offset.offset(2));
        }

        @Test
        @DisplayName("Test ConcurrentHashMap::reduceKeys method - ")
        void test_Reduce_Keys() {

            // Given
            String expectedYearList = "19-18-17-16-15";

            // When
            String yearList = this.yearlyGrossProfits.reduceKeys(Long.MAX_VALUE,
                    (y1, y2) ->  (y1.contains("-") ? y1 : y1.substring(2)) +
                            "-" + y2.substring(2));

            // Then
            assertThat(yearList)
                    .as("Should be short formatted year list: '%1$s'",
                            expectedYearList)
                    .isEqualTo(expectedYearList);
        }

        @Test
        @DisplayName("Test ConcurrentHashMap::reduceValues method - ")
        void test_Reduce_Values() {

            // Given
            Collection<Integer> grossProfits = this.yearlyGrossProfits.values();
            int expectedMaxGrossProfit = grossProfits.stream()
                    .reduce(Integer::max).get();
            int expectedMinGrossProfit = grossProfits.stream()
                    .reduce(Integer::min).get();

            // When
            Optional<Integer> maxGrossProfitOpt = Optional.ofNullable(
                    this.yearlyGrossProfits.reduceValues(
                    Long.MAX_VALUE, Integer::max));

            // Then
            assertThat(maxGrossProfitOpt.isPresent())
                    .as("Should have the max gross profit")
                    .isTrue();
            assertThat(maxGrossProfitOpt.get())
                    .as("Should be the biggest one: %1$d",
                            expectedMaxGrossProfit)
                    .isEqualTo(expectedMaxGrossProfit);
        }

        @Test
        @DisplayName("Test ConcurrentHashMap::search method - ")
        void test_Search_Method() {

            // Given
            String randomYear = this.random.nextInt(2015, 2020) + "";

            String expectedYear = randomYear + " : " +
                    this.yearlyGrossProfits.get(randomYear);

            // When
            String theRandomYearProfit = this.yearlyGrossProfits.search(
                    Long.MAX_VALUE,
                    (k, v) -> k.equals(randomYear) ? k + " : " + v : null);

            // Then
            assertThat(theRandomYearProfit)
                    .as("Should be '%1$s'", expectedYear)
                    .isEqualTo(expectedYear);
        }

        @Test
        @DisplayName("Test the mappingCount method should return a long type number - ")
        void test_Mapping_Count_Method() {

            // Given
            int count = this.yearlyGrossProfits.size();

            // When
            assertThat(count).isEqualTo(this.yearlyGrossProfits.mappingCount());
        }

    }//: End of ConcurrentHashMapReduceTest

}///:~ / 2