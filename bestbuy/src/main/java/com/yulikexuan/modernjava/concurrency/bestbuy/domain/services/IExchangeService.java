//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.services.IExchangeService.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.services;


import com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.Quote;

import javax.money.CurrencyUnit;


@FunctionalInterface
public interface IExchangeService {

    double getRate(CurrencyUnit from, CurrencyUnit to);

    static Quote applyRate(Quote quote, double rate) {
        return Quote.builder()
                .shopName(quote.getShopName())
                .currencyUnit(quote.getCurrencyUnit())
                .price(quote.getPrice() * rate)
                .discountCode(quote.getDiscountCode())
                .build();
    }

}///:~