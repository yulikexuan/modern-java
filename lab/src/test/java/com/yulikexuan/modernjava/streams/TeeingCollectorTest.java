//: com.yulikexuan.modernjava.streams.TeeingCollectorTest.java


package com.yulikexuan.modernjava.streams;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Teeing Stream Collector Test - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TeeingCollectorTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Given_String_Stream_Then_Teeing() {

        // Given
        BiFunction<String, Long, String> merger = (names, count) ->
                String.format("%s / Total: %d", names, count);

        String expectedTeeingResult = "Buddy, John, Lisa, Steve, Miles / Total: 5";

        // When
        String teeingResult = Stream.of("Buddy", "John", "Lisa", "Steve", "Miles")
                .collect(teeing(joining(", "), counting(), merger));

        // Then
        assertThat(teeingResult).isEqualTo(expectedTeeingResult);
    }

    @Test
    void test_Given_Double_Num_Stream_Then_Teeing_With_Sum_And_Average_Collectors() {

        // Given
        Map.Entry<Double, Double> stat =
                DoubleStream.of(10.0, 15.0, 20.0, 25.0, 30.0, 35.0)
                        .boxed()
                        .collect(teeing(
                                summingDouble(x -> x.doubleValue()),
                                averagingDouble(n -> n),
                                Map::entry));

        // When
        assertThat(stat).isEqualTo(Map.entry(135.0, 22.5));
    }

}///:~