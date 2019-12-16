//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.Quote.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import lombok.*;

import javax.money.CurrencyUnit;
import javax.money.Monetary;


@Getter
@EqualsAndHashCode
@ToString
@Builder @AllArgsConstructor
public class Quote {

    private final String shopName;
    private final double price;
    private final Discount.Code discountCode;
    private final CurrencyUnit currencyUnit;

    public static Quote parse(String quoteString) {

        String shopName = null;
        double price = 0;
        Discount.Code discountCode = null;
        CurrencyUnit currencyUnit = null;

        try {
            String[] split = quoteString.split(":");
            shopName = split[0];
            price = Double.parseDouble(split[1]);
            discountCode = Discount.Code.valueOf(split[2]);
            currencyUnit = Monetary.getCurrency(split[3]);
        } catch (Exception e) {
            return null;
        }

        return Quote.builder()
                .shopName(shopName)
                .price(price)
                .discountCode(discountCode)
                .build();
    }

}///:~