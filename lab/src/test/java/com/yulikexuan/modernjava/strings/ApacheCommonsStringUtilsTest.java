//: com.yulikexuan.modernjava.strings.ApacheCommonsStringUtilsTest.java


package com.yulikexuan.modernjava.strings;


import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Test StringUtils methods that don't have equivalents in String class - ")
public class ApacheCommonsStringUtilsTest {

    @Nested
    @DisplayName("The contains Methods Test")
    class ContainsMethodsTest {

        @ParameterizedTest
        @CsvSource({", *, false",
                "\" \", *, false",
                "*, null, false",
                "*,,false",
                "zzabyycdxx, za, true",
                "zzabyycdxx, zaf, true",
                "zzabyycdxx, f, false"})
        void test_Contains_Any_Char(String testString, String searchString,
                                    boolean expected) {

            // Given
            char[] chars = Objects.isNull(searchString) ? null :
                    searchString.toCharArray();

            // When
            /*
             * Checks if the CharSequence contains any character in the given set
             * of characters
             * - A null CharSequence will return false
             * - A null or zero length search array will return false.
             */
            boolean contains = StringUtils.containsAny(testString, chars);

            // Then
            assertThat(contains).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({", *, false",
                "\"  \", *, false",
                "*, null, false",
                "*,,false",
                "*, \"\", false",
                "zzabyycdxx, zab, true",
                "zzabyycdxx, zaf, true",
                "zzabyycdxx, cdx, true",
                "zzabyycdxx, fyy, true",
                "zzabyycdxx, fmm, false"})
        void test_Contains_Any_Char_Sequence(String testString, String searchString,
                                             boolean expected) {

            // Given
            if ("\"\"".equals(searchString)) {
                searchString = "";
            }

            // When
            /*
             * Checks if the CharSequence contains any character in the given set
             * of characters
             * - A null CharSequence will return false
             * - A null search CharSequence will return false.
             */
            boolean contains = StringUtils.containsAny(testString, searchString);

            // Then
            assertThat(contains).isEqualTo(expected);
        }

        /*
         * The containsIgnoreCase method checks if a given String contains another
         * String in a case insensitive manner.
         */
        @Test
        void test_Contains_Ignore_Case_Method() {

            // When
            boolean contained = StringUtils.containsIgnoreCase(
                    "baeldung.com", "BAELDUNG");

            // Then
            assertThat(contained).isTrue();
        }

        @ParameterizedTest
        @CsvSource({", *, false",
                "*,, false",
                "\"\", \"\", true",
                "*,,false",
                "abc, \"\", true",
                "zzabyycdxx, \"\", true",
                "zzabyycdxx, zab, true",
                "zzabyycdxx, cdx, true",
                "zzabyycdxx, fyy, false",
                "zzabyycdxx, fmm, false"})
        void test_Contains_Other_String_Method_Handles_Null(
                String testString, String searchString, boolean contained) {

            // Given
            if ("\"\"".equals(searchString)) {
                searchString = "";
            }

            // When
            boolean actualContained = StringUtils.contains(testString,
                    searchString);

            // Then
            assertThat(actualContained).isEqualTo(contained);
        }

        @ParameterizedTest
        @CsvSource({", *, false",
                "\"\", *, false",
                "\"\", \"\", true",
                "zzabyycdxx, c, true",
                "zzabyycdxx, d, true"})
        void test_Contains_Char_Method_Handles_Null(
                String testString, String searchChar, boolean contained) {

            // Given
            char anyChar = searchChar.charAt(0);

            // When
            boolean actualContained = StringUtils.contains(testString, anyChar);

            // Then
            assertThat(actualContained).isEqualTo(contained);
        }

    }//: End of class ContainsAnyTest

    @Test
    void test_Count_Matches_Method() {

        // Given
        String testString = "welcome to www.baeldung.com";

        // When
        int charNum = StringUtils.countMatches(testString, 'w');
        int starNum = StringUtils.countMatches(testString, '*');
        int dotComNum = StringUtils.countMatches(testString, ".com");

        // Then
        assertThat(charNum).isEqualTo(4);
        assertThat(starNum).isEqualTo(0);
        assertThat(dotComNum).isEqualTo(1);
    }

}///:~