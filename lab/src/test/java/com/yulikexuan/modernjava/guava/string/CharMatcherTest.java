//: com.yulikexuan.modernjava.guava.string.CharMatcherTest.java


package com.yulikexuan.modernjava.guava.string;


import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


public class CharMatcherTest {

    private String input;
    private String result;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("Able to remove all special characters - ")
    @Test
    void test_Removing_Special_Characters() {

        // Given
        input = "H*el.lo,}12";

        // When
        result = CharMatcher.inRange('a', 'z')
                .or(CharMatcher.inRange('A', 'Z')
                .or(CharMatcher.inRange('0', '9')))
                // Returns a string containing all matching BMP (the Basic
                // Multilingual Plane of Unicode) characters of a character
                // sequence, in order
                .retainFrom(input);

        // Then
        assertThat(result).as("All special characters should be removed")
                .isEqualTo("Hello12");
    }

    @Test
    @DisplayName("Able to remove all non ASCII characters - ")
    void test_Removing_Non_ASCII() {

        // Givne
        input = "あhello₤";

        // When
        result = CharMatcher.ascii().retainFrom(input);

        // Then
        assertThat(result).as("All non ASCII characters have " +
                "been removed").isEqualTo("hello");
    }

    /*
     * Code page 437 is the character set of the original IBM PC (personal computer).
     * It is also known as CP437, OEM-US, OEM 437, PC-8, or DOS Latin US.
     *
     * The set includes all printable ASCII characters, extended codes for
     * accented letters (diacritics), some Greek letters, icons, and
     * line-drawing symbols.
     *
     * It is sometimes referred to as the "OEM font" or "high ASCII", or as
     * "extended ASCII" (one of many mutually incompatible ASCII extensions)
     */
    @Test
    @DisplayName("Able to remove all non cp437 characters - ")
    void able_To_Removing_Characters_Not_In_The_CP437_Charset() {

        // Given
        Charset charset = Charset.forName("cp437");
        CharsetEncoder encoder = charset.newEncoder();

        Predicate<Character> inRange = c -> encoder.canEncode(c);

        // When
        result = CharMatcher.forPredicate(inRange).retainFrom("helloは");

        // Then
        assertThat(result).as("All non cp437 characters have " +
                "been removed").isEqualTo("hello");
    }

    @Test
    @DisplayName("Able to validate String - ")
    void test_Validating_String() {

        // Given
        input = "hello";

        // When & Then
        assertAll("Test validating String - ",
                () -> assertThat(CharMatcher.inRange('a', 'z')
                        .matchesAllOf(input)).isTrue(),
                () -> assertThat(CharMatcher.is('e')
                        .matchesAnyOf(input)).isTrue(),
                () -> assertThat(CharMatcher.inRange('0', '9')
                        .matchesNoneOf(input)).isTrue());
    }

    @Test
    @DisplayName("Able to trimming String - ")
    void test_Trimming_String() {

        // Given
        input = "---hello,,,";

        // When
        result = CharMatcher.is('-').trimLeadingFrom(input);

        // Then
        assertAll("Test trimming String - ",
                () -> assertThat(CharMatcher.is('-').trimLeadingFrom(input))
                        .as("Leading '-' characters should " +
                                "be removed")
                        .isEqualTo("hello,,,"),
                () -> assertThat(CharMatcher.is(',').trimTrailingFrom(input))
                        .as("Trailing ',' characters should " +
                                "be removed")
                        .isEqualTo("---hello"),
                () -> assertThat(CharMatcher.anyOf("-,").trimFrom(input))
                        .as("Both of leading and trailing " +
                                "characters should be removed")
                        .isEqualTo("hello"));
    }

    @Test
    @DisplayName("Able to collapse a String - ")
    void test_Collapse_String() {

        // Given
        input = "    He    llo    ";

        // When & Then
        assertAll("All collapse operation should work",
                () -> assertThat(CharMatcher.is(' ')
                        .collapseFrom(input, '-'))
                        .isEqualTo("-He-llo-"),
                () -> assertThat(CharMatcher.is(' ')
                        .trimAndCollapseFrom(input, '*'))
                        .isEqualTo("He*llo")
        );
    }

    @Test
    @DisplayName("Able to replace from a String - ")
    void test_Replace_From_A_String() {

        // Given
        input = "Apple-banana.";

        // When & Then
        assertThat(CharMatcher.is('-').replaceFrom(input, " and "))
                .as("Should contain 'and'")
                .isEqualTo("Apple and banana.");

        assertThat(CharMatcher.anyOf("-.").replaceFrom(input, ' '))
                .as("Should be separated by ' '")
                .isEqualTo("Apple banana ");
    }

    @Test
    @DisplayName("Able to count character occurrence - ")
    void test_Counting_Char_Occurrence() {

        // Given
        input = "a, c, z, 1, 2, 7, ";

        // When
        int commaOccurrence = CharMatcher.is(',').countIn(input);

        // Both endpoints are inclusive
        int letterOccurrence = CharMatcher.inRange('a', 'h').countIn(input);
        int digitOccurrence = CharMatcher.inRange('0', '5').countIn(input);

        // Then
        assertThat(commaOccurrence).as("Comma occurrence should be 6.")
                .isEqualTo(6);

        assertThat(letterOccurrence)
                .as("Should have 2 chars being from 'a' to 'h'.")
                .isEqualTo(2);

        assertThat(digitOccurrence)
                .as("Should have 2 digits being from '0' to '5'.")
                .isEqualTo(2);
    }

}///:~