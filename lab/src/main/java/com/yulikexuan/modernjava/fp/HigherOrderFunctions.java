//: com.yulikexuan.modernjava.fp.HigherOrderFunctions.java


package com.yulikexuan.modernjava.fp;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Getter
@Builder
@ToString
@RequiredArgsConstructor
class Apple {
    private final String name;
    private final double price;
}

class FruitInventory {

    static Comparator<Apple> applePriceReversedComparator =
            Comparator.comparing(Apple::getPrice).reversed();

    static Comparator<Apple> appleNameComparator =
            Comparator.comparing(Apple::getName);

    public static List<Apple> getAppleInventory() {
        return List.of(
                Apple.builder().name("Gala").price(2.09).build(),
                Apple.builder().name("MCINTOSH").price(2.29).build(),
                Apple.builder().name("Empire").price(1.99).build(),
                Apple.builder().name("Red_Delicious").price(2.59).build());
    }
}

/*
 * Functions (such as Comparator.comparing) that can do at least one of the
 * following are called higher-order functions
 *     - Take one or more functions as a parameter
 *     - Return a function as a result
 */
public class HigherOrderFunctions {

    public static void main(String[] args) {

        // List::sort is a Higher-Order Function
        // Comparator::comparing is a Higher-Order Function

        List<Apple> apples = new ArrayList<>(FruitInventory.getAppleInventory());
        apples.sort(FruitInventory.applePriceReversedComparator);
        System.out.println(apples);

        apples.sort(FruitInventory.appleNameComparator);
        System.out.println(apples);
    }

}///:~