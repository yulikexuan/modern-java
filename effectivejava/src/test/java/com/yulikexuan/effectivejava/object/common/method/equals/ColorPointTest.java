//: com.yulikexuan.effectivejava.object.common.method.equals.ColorPointTest.java


package com.yulikexuan.effectivejava.object.common.method.equals;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
@DisplayName("Test Equals Methods of SubClasses - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ColorPointTest {

    private int x;
    private int y;
    private Color color;

    private Point point;

    @BeforeEach
    void setUp() {
        this.x = 1;
        this.y = 2;
        this.color = Color.RED;
        this.point = Point.of(this.x, this.y);
    }

    @Test
    void test_Non_Symmetry_Equals_ColorPoint() {

        // Given
        ColorPointWithNonSymmetryEquals colorPoint =
                ColorPointWithNonSymmetryEquals.of(1, 2, this.color);

        // When
        boolean pointEqualsColorPoint = this.point.equals(colorPoint);
        boolean colorPointEqualsPoint = colorPoint.equals(point);

        // Then
        assertThat(pointEqualsColorPoint).isTrue();
        assertThat(colorPointEqualsPoint).isFalse();
    }

    @Test
    void test_Non_Transitive_Equals_ColorPoint() {

        // Given
        ColorPointWithNonTransitiveEquals redPoint =
                ColorPointWithNonTransitiveEquals.of(1, 2, this.color);

        ColorPointWithNonTransitiveEquals bluePoint =
                ColorPointWithNonTransitiveEquals.of(1, 2, Color.BLUE);

        // When
        boolean redPointEqualsPoint = redPoint.equals(this.point);
        boolean pointEqualsBluePoint =  this.point.equals(bluePoint);
        boolean bluePointEqualsRedPoint = bluePoint.equals(redPoint);

        // Then
        assertThat(redPointEqualsPoint).isTrue();
        assertThat(pointEqualsBluePoint).isTrue();
        assertThat(bluePointEqualsRedPoint).isFalse();
    }

    @Test
    void test_Non_Transitive_Equals_Between_Color_And_Smell_Point() {

        // Given
        SmellPointWithNonTransitiveEquals applePoint =
                SmellPointWithNonTransitiveEquals.of(1, 2, Smell.APPLE);

        ColorPointWithNonTransitiveEquals redPoint =
                ColorPointWithNonTransitiveEquals.of(1, 2, this.color);

        // When & Then
        assertThatThrownBy(() -> applePoint.equals(redPoint))
                .isInstanceOf(StackOverflowError.class);
    }

}///:~