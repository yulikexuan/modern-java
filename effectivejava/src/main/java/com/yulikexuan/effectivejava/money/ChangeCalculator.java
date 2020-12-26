//: com.yulikexuan.effectivejava.money.ChangeCalculator.java

package com.yulikexuan.effectivejava.money;


import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.math.BigDecimal;


class ChangeCalculator {

    // Broken - uses floating point for monetary calculation!
    public static ImmutablePair<Double, Integer> calculate(double funds) {

        int itemsBought = 0;

        for (double price = 0.10; funds >= price; price += 0.10) {
            funds -= price;
            itemsBought++;
        }

        return ImmutablePair.of(funds, itemsBought);
    }

    static final BigDecimal TEN_CENTS = new BigDecimal("0.10");

    public static ImmutablePair<BigDecimal, Integer> calculateWithBigDecimal(
            @NonNull BigDecimal funds) {

        int itemsBought = 0;

        for (BigDecimal price = TEN_CENTS; funds.compareTo(price) >= 0;
             price = price.add(TEN_CENTS)) {

            funds = funds.subtract(price);
            itemsBought++;
        }

        return ImmutablePair.of(funds, itemsBought);
    }

}///:~