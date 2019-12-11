//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.Quote.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import lombok.*;

import java.util.Optional;


@Getter
@EqualsAndHashCode
@ToString
@Builder @AllArgsConstructor
public class Quote {

    private final String shopName;
    private final double price;
    private final Discount.Code discountCode;

    public static Quote parse(String quoteString) {

        String shopName = null;
        double price = 0;
        Discount.Code discountCode = null;

        try {
            String[] split = quoteString.split(":");
            shopName = split[0];
            price = Double.parseDouble(split[1]);
            discountCode = Discount.Code.valueOf(split[2]);
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