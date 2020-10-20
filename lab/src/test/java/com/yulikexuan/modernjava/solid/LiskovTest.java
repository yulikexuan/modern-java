//: com.yulikexuan.modernjava.solid.LiskovTest.java


package com.yulikexuan.modernjava.solid;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


/**
 * The class demonstrates the Liskov Substitution Principle (LSP)
 *
 * As per the principle, the functions that use references to the base
 * classes must be able to use objects of derived class without knowing it
 *
 * Thus, in the example shown below, the function calculateArea which uses the
 * reference of "Rectangle" should be able to use the objects of derived class
 * such as Square and fulfill the requirement posed by Rectangle definition
 *
 * One should note that as per the definition of Rectangle, following must
 * always hold true given the data below:
 *   1. Length must always be equal to the length passed as the input to method,
 *      setLength
 *   2. Breadth must always be equal to the breadth passed as input to method,
 *      setBreadth
 *   3. Area must always be equal to product of length and breadth
 *
 * In case, we try to establish ISA relationship between Square and Rectangle
 * such that we call "Square is a Rectangle", below code would start behaving
 * unexpectedly if an instance of Square is passed
 *
 * Assertion error will be thrown in case of check for area and check for
 * breadth, although the program will terminate as the assertion error is
 * thrown due to failure of Area check
 *
 * @author Ajitesh Shukla
 */
@Slf4j
@DisplayName("Liskov Principle Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LiskovTest {

    private int length;
    private int breadth;

    private int expectedArea;

    private Rectangle rectangle;
    private Square square;

    private Rectangle testObj;

    @BeforeEach
    void setUp() {

        this.length = 2;
        this.breadth = 3;
        this.expectedArea = 6;

        this.rectangle = new Rectangle();
        this.rectangle.setLength(this.length);
        this.rectangle.setBreadth(this.breadth);

        this.square = new Square();
        this.square.setLength(this.length);
        this.square.setBreadth(this.breadth);
    }

    @Test
    void test_Given_Rectangle_When_Validating_Liskov_Principle_Then_Passed() {

        this.testLiskov(this.rectangle, () -> {
            assertThat(this.rectangle.getArea())
                    .as(">>> The actual area should be %d",
                            this.expectedArea)
                    .isEqualTo(this.expectedArea);
            assertThat(this.rectangle.getLength())
                    .as(">>> The actual length should be %d",
                            this.length)
                    .isEqualTo(this.length);
            assertThat(this.rectangle.getBreadth())
                    .as(">>> The actual breadth should be %d",
                            this.breadth)
                    .isEqualTo(this.breadth);
        });
    }

    @Test
    void test_Given_Square_When_Validating_Liskov_Principle_Then_Passed() {
        this.testLiskov(this.square, () -> {
            assertThat(this.square.getArea())
                    .as(">>> The actual area should not be %d",
                            this.expectedArea)
                    .isNotEqualTo(this.expectedArea);
            assertThat(this.square.getLength())
                    .as(">>> The actual length should not be %d",
                            this.length)
                    .isNotEqualTo(this.length);
            assertThat(this.square.getBreadth())
                    .as(">>> The actual breadth should be %d",
                            this.breadth)
                    .isEqualTo(this.breadth);
        });
    }

    private void testLiskov(Rectangle testTarget, Executable assertions) {

        // Given
        int actualArea = testTarget.getArea();

        int actualLength = testTarget.getLength();
        int actualBreadth = testTarget.getBreadth();

        // When & Then
        assertAll("", assertions);
    }

}///:~