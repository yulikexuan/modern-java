//: com.yulikexuan.effectivejava.enums.enumset.TextTest.java


package com.yulikexuan.effectivejava.enums.enumset;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static com.yulikexuan.effectivejava.enums.enumset.Text.Style;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test EnumSet of Text Styles - ")
class TextTest {

    private Text text;

    @BeforeEach
    void setUp() {
        this.text = new Text();
    }

    @Test
    void test_Applying_EnumSet_Of_Text_Styles() {

        // Given
        EnumSet<Style> allTextStyles = EnumSet.allOf(Style.class);

        // When
        int howManyStylesApplied = text.applyStyles(allTextStyles);

        // Then
        assertThat(howManyStylesApplied).isEqualTo(Style.values().length);
    }

}///:~