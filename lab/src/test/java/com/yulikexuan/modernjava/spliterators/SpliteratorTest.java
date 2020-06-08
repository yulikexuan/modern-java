//: com.yulikexuan.modernjava.spliterators.SpliteratorTest.java


package com.yulikexuan.modernjava.spliterators;


import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


/*
 * Spliterators, like Iterators, are for traversing the elements of a source
 *
 * The Spliterator API was designed to support efficient parallel traversal in
 * addition to sequential traversal, by supporting decomposition as well as
 * single-element iteration
 *
 * In addition, the protocol for accessing elements via a Spliterator is
 * designed to impose smaller per-element overhead than Iterator, and to avoid
 * the inherent race involved in having separate methods for hasNext() and next()
 *
 *
 *
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SpliteratorTest {

    static final int NUMBER_OF_NAMES = 17;

    @Mock
    private Consumer<String> nameConsumer;

    @Mock
    private Consumer<String> nameConsumer_2;

    private List<String> randomNames;
    private Spliterator<String> spliterator_1;

    @BeforeEach
    void setUp() {
        randomNames = Stream.generate(
                () -> RandomStringUtils.randomAlphanumeric(7))
                .limit(NUMBER_OF_NAMES)
                .collect(ImmutableList.toImmutableList());
        spliterator_1 = randomNames.spliterator();
    }

    @Nested
    @DisplayName("Spliterator's basic usage test - ")
    class BasicUsagesTest {

        @Test
        void test_Given_A_List_Then_Iterate_Each_Element_With_Spliterator() {

            // Given

            // When
            while (spliterator_1.tryAdvance(nameConsumer)) {}

            // Then
            then(nameConsumer).should(times(NUMBER_OF_NAMES)).accept(anyString());
        }

        @Test
        void test_Given_A_List_Then_Split_Twice_And_Traverse_Each() {

            // Given
            Spliterator<String> namesSpliterator_2 = spliterator_1.trySplit();

            int estimatedNum_1 = (int) spliterator_1.estimateSize();
            int estimatedNum_2 = (int) namesSpliterator_2.estimateSize();

            // When
            while (spliterator_1.tryAdvance(nameConsumer)) {}
            while (namesSpliterator_2.tryAdvance(nameConsumer_2)) {}

            // Then
            then(nameConsumer).should(
                    times(NUMBER_OF_NAMES / 2 + 1))
                    .accept(anyString());
            then(nameConsumer_2).should(
                    times(NUMBER_OF_NAMES / 2))
                    .accept(anyString());

            assertThat(estimatedNum_1).isEqualTo(NUMBER_OF_NAMES / 2 + 1);
            assertThat(estimatedNum_2).isEqualTo(NUMBER_OF_NAMES / 2);
        }

        @Test
        void test_Given_Spliterator_Then_Fetch_Its_Characters() {

            // Given

            // When
            Set<SpliteratorCharacteristic> characteristics =
                    SpliteratorCharacteristic.getCharacteristics(spliterator_1);

            // Then
            // System.out.println(characteristics);
            assertThat(characteristics).containsExactlyInAnyOrder(
                    SpliteratorCharacteristic.SIZED,
                    SpliteratorCharacteristic.ORDERED,
                    SpliteratorCharacteristic.IMMUTABLE,
                    SpliteratorCharacteristic.SUBSIZED,
                    SpliteratorCharacteristic.NONNULL);
        }

    } //: End of class BasicUsagesTest

    @Nested
    @DisplayName("Splilterator Implementation Test - ")
    class CustomizedSpliteratorTest {

        static final String SENTENCE =
                "  Nel mezzo del   cammin di nostra vita mi ritrovai " +
                        "in una selva oscura ché la dritta via era smarrita ";
        static final int EXPECTED_NUM_OF_WORDS = 19;

        @Test
        void test_Given_String_Then_Count_Words_Sequentially() {

            // Given
            Stream<Character> characters = IntStream.range(0, SENTENCE.length())
                    .mapToObj(SENTENCE::charAt);

            // When
            WordCounter finalWordCounter = characters.reduce(
                    WordCounter.of(0, true),
                    WordCounter::accumulate, WordCounter::combine);

            // Then
            assertThat(finalWordCounter.getCounter()).isEqualTo(
                    EXPECTED_NUM_OF_WORDS);
        }

        @Test
        void test_Given_String_Then_Count_Words_Concurrently_Failed() {

            // Given
            Stream<Character> characters = IntStream.range(0, SENTENCE.length())
                    .mapToObj(SENTENCE::charAt);

            // When
            WordCounter finalWordCounter = characters.parallel().reduce(
                    WordCounter.of(0, true),
                    WordCounter::accumulate, WordCounter::combine);

            // Then
            assertThat(finalWordCounter.getCounter()).isNotEqualTo(
                    EXPECTED_NUM_OF_WORDS);
        }

        @Test
        void test_Given_String_And_Spliterator_Then_Count_Words_Concurrently() {

            // Given
            WordCounterSpliterator spliterator =
                    WordCounterSpliterator.of(SENTENCE, 50);

            Stream<Character> characterStream = StreamSupport.stream(
                    spliterator, true);

            // When
            StopWatch stopWatch = StopWatch.createStarted();
            WordCounter wordCounter = characterStream.reduce(
                    WordCounter.of(0, true),
                    WordCounter::accumulate, WordCounter::combine);

            // Then
            stopWatch.stop();
            assertThat(stopWatch.getTime()).isLessThan(500L);
            assertThat(wordCounter.getCounter()).isEqualTo(EXPECTED_NUM_OF_WORDS);
        }

    }//: End of class CustomizedSpliteratorTest

}///:~