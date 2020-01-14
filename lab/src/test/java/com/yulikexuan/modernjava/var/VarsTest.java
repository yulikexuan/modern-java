//: com.yulikexuan.modernjava.var.VarsTest.java


package com.yulikexuan.modernjava.var;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class VarsTest {


    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Vars() {

        // Given
        var personId = 1001;
        var prompt = "Enter a message: ";
        var luckyNums = List.of(7, 17, 1967);
        var cities = new String[] {
                "Atlanta",
                "Patna",
                "Paris",
                "Gaya"
        };

        BiFunction<Integer, Long, Double> avg =
                (var n1, var n2) -> (n1 + n2) / 2.0;

        // When & %Then
        assertAll("Inferred int",
                () -> assertThat(personId).isInstanceOf(Integer.class),
                () -> assertThat(personId).isEqualTo(1001));

        assertAll("Inferred String",
                () -> assertThat(prompt).isInstanceOf(String.class),
                () -> assertThat(prompt.length()).isEqualTo(17),
                () -> assertThat(prompt.substring(0, 5)).isEqualTo("Enter"));

        assertAll("Inferred List",
                () -> assertThat(luckyNums)
                        .startsWith(7)
                        .endsWith(1967)
                        .contains(17));

        assertAll("Inferred String Array",
                () -> assertThat(cities).isInstanceOf(String[].class),
                () -> assertThat(Arrays.toString(cities)).isEqualTo(
                        "[Atlanta, Patna, Paris, Gaya]"));

        for (var i = 0; i < cities.length; i++) {
            assertThat(cities[i]).isIn(cities);
        }

        for (var city: cities) {
            assertThat(city).isIn(cities);
        }


        assertThat(avg.apply(10, 20L)).isEqualTo(15.0);
    }

    @Test
    void test_Inferring_Non_Denotable_Types() {

        // Given
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(">>>>>>> Inside run() method ...");
            }
            public void execute() {
                System.out.println(">>>>>>> Inside execute() method ...");
            }
        };

        // runnable.execute(); // Not possible

        /*
         * Declare a non-denotable type of the anonymous class
         * In this case, compiler infers the non-denotable type of the
         * anonymous class
         */
        final int number = 2020;
        var runnable2 = new Runnable() {

            int num;

            @Override
            public void run() {
                System.out.println(">>>>>>> Inside run() method ...");
            }

            public void execute() {
                this.num = number;
            }
        };

        // When
        runnable2.execute();

        // Then
        assertThat(runnable2.num).isEqualTo(number);
    }

}///:~