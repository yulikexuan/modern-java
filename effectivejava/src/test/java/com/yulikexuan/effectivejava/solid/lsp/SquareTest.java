//: com.yulikexuan.effectivejava.solid.lsp.SquareTest.java


package com.yulikexuan.effectivejava.solid.lsp;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * The class demonstrates the Liskov Substitution Principle (LSP)
 *
 * As per the principle, the functions that use references to the base classes
 * must be able to use objects of derived class without knowing it
 *
 * Thus, in the example shown below, the function calculateArea which uses the
 * reference of "Rectangle" should be able to use the objects of derived class
 * such as Square and fulfill the requirement posed by Rectangle definition
 *
 */
@Slf4j
@DisplayName("Test LSP of SOLID - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LspTest {

    private Rectangle rectangle;
    private Square square;

    private int length;
    private int breadth;
    private long expectedArea;

    @BeforeEach
    void setUp() {
        this.length = 3;
        this.breadth = 2;
        this.rectangle = new Rectangle();
        this.square = new Square();
        this.expectedArea = this.length * this.breadth;
    }

    private long calculateArea(Rectangle rectangle) {

        rectangle.setLength(this.length);
        rectangle.setBreadth(this.breadth);

        log.info(">>>>>> The length is {}", rectangle.getLength());
        log.info(">>>>>> The breadth is {}", rectangle.getBreadth());

        long actualArea = this.rectangle.getArea();

        if (this.expectedArea != actualArea) {
            throw new ViolatingLspException(String.format(
                    "The area should be %d", this.expectedArea));
        }

        return actualArea;
    }

    @Test
    void test_Area_Calculation_Of_Rectangle() {

        // Given

        // When
        long area = this.calculateArea(this.rectangle);

        // Then
        assertThat(area).isEqualTo(this.expectedArea);
    }

    @Test
    void test_Area_Calculation_Of_Square() {
        assertThatThrownBy(() -> this.calculateArea(this.square))
                .isInstanceOf(ViolatingLspException.class);
    }

}///:~