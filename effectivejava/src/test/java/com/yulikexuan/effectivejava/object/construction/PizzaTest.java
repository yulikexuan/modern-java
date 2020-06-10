//: com.yulikexuan.effectivejava.object.construction.PizzaTest.java


package com.yulikexuan.effectivejava.object.construction;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.yulikexuan.effectivejava.object.construction.NyPizza.Size.MEDIUM;
import static com.yulikexuan.effectivejava.object.construction.Pizza.Topping.MUSHROOM;
import static com.yulikexuan.effectivejava.object.construction.Pizza.Topping.ONION;
import static org.assertj.core.api.Assertions.assertThat;


class PizzaTest {


    @BeforeEach
    void setUp() {

    }

    @Test
    void test_Given_New_York_Pizza_Builder_Then_Build_NyPizza() {

        // Given
        NyPizza nyPizza = NyPizza.builder(MEDIUM)
                .addTopping(MUSHROOM)
                .addTopping(ONION)
                .build();


        // When & Then
        assertThat(nyPizza.getToppings()).contains(MUSHROOM, ONION);
        assertThat(nyPizza.getSize()).isSameAs(MEDIUM);
    }

}///:~