//: com.yulikexuan.modernjava.strings.NewStringMethodsTest.java


package com.yulikexuan.modernjava.strings;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

/*
 * Since JDK 8
 *   - static String join(CharSequence delimiter, CharSequence... elements)
 *   - static String join(CharSequence delimiter,
 *                        Iterable<? extends CharSequence> elements)
 *
 * Since JDK 9
 *   - IntStream chars()
 *   - IntStream codePoints()
 *
 * Since JDK 11
 *   - String strip()
 *   - String stripLeading()
 *   - String stripTrailing()
 *   - boolean isBlank()
 *   - Stream<String> lines()
 *   - String repeat​(int count)
 *
 * Since JDK 12
 *   - String indent​(int n)
 *   - <R> R transform​(Function<? super String,​? extends R> f)
 *
 * Since JDK 13
 *   - String stripIndent()
 *     // Deprecated, for removal: This API element is subject to removal in a future version.
 *
 *   - String translateEscapes()
 *     // Deprecated, for removal: This API element is subject to removal in a future version.
 *
 *   - String formatted​(Object... args)
 *     // Deprecated, for removal: This API element is subject to removal in a future version.
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Test new methods of String since JDK 8 - ")
public class NewStringMethodsTest {

    @Test
    void test_Given_String_When_Trim_And_Strip_Then_Compare() {

        // Given
        String testStr_1 = " \u2000 Testing 1 2 3 \u2000 ";

        // When
        String result_of_strip = testStr_1.strip();
        String result_of_trim = testStr_1.trim();

        // Then
        System.out.printf("By strip: '%s'%n", result_of_strip);
        System.out.printf("By trim: '%s'%n", result_of_trim);
    }

    @Test
    void test_Given_String_Then_Validate_It_With_isEmpty_And_isBlank() {

        // Given
        String testBlank = " \t\n    \f\r ";

        // When
        boolean isBlank = testBlank.isBlank();
        boolean isEmpty = testBlank.isEmpty();

        // Then
        assertThat(isBlank).isTrue();
        assertThat(isEmpty).isFalse();
    }

    @Test
    void test_Given_A_String_Then_Retrive_Stream_Of_Lines_In_That_String() {

        // Given
        Consumer<String> lineConsumer = mock(Consumer.class);
        String textBlock = "test\r\nGiven\rA\nString\r\nThen\rRetrive\r" +
                "Stream\nOf\nLines\rIn\rThat\r\nString\r\n";

        // When
        textBlock.lines()
                .forEach(lineConsumer::accept);

        // Then
        then(lineConsumer).should(times(12))
                .accept(anyString());
    }

    @Test
    void test_Given_String_Block_Then_Add_Dash_Indentation_To_Each_Line() {

        // Given
        String doc = "1 Header1\r\n1.1 Header2\r\n1.1.1 Header3\r\nContent";
        String[] lines = doc.lines().toArray(String[]::new);

        // When
        String formattedDoc = IntStream.range(0, lines.length)
                .mapToObj(i -> "-".repeat(i * 4) + lines[i] + "\r\n")
                .collect(Collectors.joining());

        // Then
        assertThat(formattedDoc.length()).isEqualTo(72);
    }

    /*
     * String:: indent(int n)
     *
     * Adjusts the indentation of each line of this string based on the value
     * of n, and normalizes line termination characters
     *
     *   - This string is conceptually separated into lines using lines()
     *   - Each line is then adjusted as described below and then suffixed with
     *     a line feed "\n" (U+000A). The resulting lines are then concatenated
     *     and returned
     *   - If n > 0 then n spaces (U+0020) are inserted at the beginning of
     *     each line
     *   - If n < 0 then up to n white space characters are removed from the
     *     beginning of each line. If a given line does not contain sufficient
     *     white space then all leading white space characters are removed
     *   - Each white space character is treated as a single character
     *   - In particular, the tab character "\t" (U+0009) is considered a single
     *     character; it is not expanded
     *   - If n == 0 then the line remains unchanged.
     *     However, line terminators are still normalized
     */
    @Test
    void test_Given_String_Block_Then_Add_Space_Indentation_To_Each_Line() {

        // Given
        String doc = "1 Header1\r\n1.1 Header2\r\n1.1.1 Header3\r\nContent\r\n";
        int length = doc.length();

        // When
        String formattedDoc = doc.indent(4);
        int newLength = formattedDoc.length();

        // Then
        assertThat(newLength - length).isEqualTo(12);
    }

    @Test
    void test_Given_String_Block_Then_Strip_Each_Line_And_Add_Line_Num_To_Each() {

        // Given
        UnaryOperator<String> stringTransformer_1 =
                StringTransformation::sanitizeBlanks;

        Function<String, String> stringFinalTransformer =
                stringTransformer_1.andThen(StringTransformation::addLineNumber);

        String testValue = "    Upon the moon I fixed my eye,    \n\n       All over the wide lea;             \nWith quickening pace my horse drew nigh     \n\n        Those paths so dear to me.        \n";

        // When
        String actualProcessedString = testValue.transform(stringFinalTransformer);
        String[] lines = actualProcessedString.lines().toArray(String[]::new);

        // Then
        //System.out.println(actualProcessedString);
        assertThat(lines).allMatch(line -> line.matches(
                "\\d+\\s:\\s[\\w\\s]+\\W*"));
    }

}///:~