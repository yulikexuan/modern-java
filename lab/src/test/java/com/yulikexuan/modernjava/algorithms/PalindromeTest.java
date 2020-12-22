//: com.yulikexuan.modernjava.algorithms.PalindromeTest.java

package com.yulikexuan.modernjava.algorithms;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Palindrome Word - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PalindromeTest {

    @Test
    void test_Permutation() {

        // Given

        // When
        assertThat(Palindrome.isPalindrome("l")).isTrue();
        assertThat(Palindrome.isPalindrome("ll")).isTrue();
        assertThat(Palindrome.isPalindrome("la")).isFalse();
        assertThat(Palindrome.isPalindrome("llaazz")).isTrue();
        assertThat(Palindrome.isPalindrome("llllaaaazzz")).isTrue();
        assertThat(Palindrome.isPalindrome("lllll")).isTrue();
        assertThat(Palindrome.isPalindrome("")).isTrue();
    }

}///:~