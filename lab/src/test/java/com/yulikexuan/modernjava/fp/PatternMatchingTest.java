//: com.yulikexuan.modernjava.fp.PatternMatchingTest.java


package com.yulikexuan.modernjava.fp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PatternMatchingTest {


    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Pattern_Matching_For_Simplifying_Expression() {

        // Given
        Expr e = new BinOp("+", new Number(5), new Number(0));

        // When
        Expr match = PatternMatching.simplify(e);

        // Then
        assertThat(match.getValue()).isEqualTo(5);
    }

}///:~