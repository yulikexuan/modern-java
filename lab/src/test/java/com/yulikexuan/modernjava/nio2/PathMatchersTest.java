//: com.yulikexuan.modernjava.nio2.PathMatchersTest.java


package com.yulikexuan.modernjava.nio2;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("PathMatcher Tests - ")
class PathMatchersTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test asterisk marks in glob expressions - ")
    void test_Glob_Asterisks_Match() {

        // Given
        Path filePath = Paths.get("/com/java/One.java");

        // When & Then
        assertAll(
                "One asterisk matches any character except for a dir " +
                        "boundary, two asterisks match any character, " +
                        "including a dir boundary",
                () -> assertThat(PathMatchers.matches(
                        filePath, "glob:*.java")).isFalse(),
                () -> assertThat(PathMatchers.matches(filePath,
                        "glob:**/*.java"))
                        .isTrue(),
                () -> assertThat(PathMatchers.matches(filePath, "glob:*"))
                        .isFalse(),
                () -> assertThat(PathMatchers.matches(filePath, "glob:**"))
                        .isTrue());
    }

    @Test
    @DisplayName("Test question marks in glob expressions - ")
    void test_Glob_Question_Marks_Match() {

        // Given
        Path filePath_1 = Paths.get("One.java");
        Path filePath_2 = Paths.get("One.ja^a");

        // When & Then
        assertAll("A question mark mathces any character. A character " +
                        " could be a letter or a number or anything else;",
                () -> assertThat(PathMatchers.matches(filePath_1,
                        "glob:*.????")).isTrue(),
                () -> assertThat(PathMatchers.matches(filePath_1,
                        "glob:*.???")).isFalse(),
                () -> assertThat(PathMatchers.matches(filePath_2,
                        "glob:*.????")).isTrue(),
                () -> assertThat(PathMatchers.matches(filePath_2,
                        "glob:*.???")).isFalse());
    }

    @Test
    @DisplayName("Test multiple glob expressions in one - ")
    void test_Multiple_Patterns_Matching() {

        // Given
        Path path_1 = Paths.get("Bert-book");
        Path path_2 = Paths.get("Kathy-horse");

        // When & Then
        assertAll("With wildcards inside or outside of curly braces " +
                        "to have multiple glob expressions",
                () -> assertThat(PathMatchers.matches(
                        path_1, "glob:{Bert*,Kathy*}")).isTrue(),
                () -> assertThat(PathMatchers.matches(
                        path_2, "glob:{Bert,Kathy}*")).isTrue(),
                () -> assertThat(PathMatchers.matches(
                        path_1, "glob:{Bert,Kathy}")).isFalse());
    }

    @Test
    void test_Character_Sets_In_Glob_Expression() {

        // Given &
        String glob = "glob:[0-9]\\^{A*,b}/**/1";

        // When & Then
        assertAll("Able to use sets of characters in glob pattern",
                () -> assertThat(PathMatchers.matches(
                        Paths.get("0^b/test/1"), glob)).isTrue(),
                () -> assertThat(PathMatchers.matches(
                        Paths.get("9\\^b/test/1"), glob)).isFalse(),
                () -> assertThat(PathMatchers.matches(
                        Paths.get("01b/test/1"), glob)).isFalse(),
                () -> assertThat(PathMatchers.matches(
                        Paths.get("0^b/1"), glob)).isFalse());
    }

}///:~