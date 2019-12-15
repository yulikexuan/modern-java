//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.services.ExchangeService.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.services;


import com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.IShop;
import com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.IShopping;

import javax.money.CurrencyUnit;


public class ExchangeService implements IExchangeService {

    @Override
    public double getRate(CurrencyUnit from, CurrencyUnit to) {
        IShop.delay();
        return IShopping.RANDOM.nextDouble(0.85, 1.15);
    }

}///:~