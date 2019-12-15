//: com.yulikexuan.modernjava.concurrency.bestbuy.domain.model.Discount.java


package com.yulikexuan.modernjava.concurrency.bestbuy.domain.model;


import org.apache.commons.math3.util.Precision;

public class Discount {

    static final String PRICE_TEMPLATE = "%1$s price is %2$s %3$.2f [%4$s]";

    public enum Code {

        NONE(0),
        SILVER(5),
        GOLD(10),
        PLATINUM(15),
        DIAMOND(20);

        private final int percentage;

        private Code(int percentage) {
            this.percentage = percentage;
        }

        public int getPercentage() {
            return this.percentage;
        }

    } //: End of Discount.Code

    public static Quote applyDiscount(final Quote quote) {

        if (quote == null) {
            return null;
        }

        return Quote.builder()
                .shopName(quote.getShopName())
                .currencyUnit(quote.getCurrencyUnit())
                .price(Discount.apply(quote.getPrice(), quote.getDiscountCode()))
                .discountCode(quote.getDiscountCode()).build();
    }

    private static double apply(double price, Code code) {
        IShop.delay();
        double finalPrice = price * (100 - code.getPercentage() / 100);
        return Precision.round(finalPrice, 2);
    }

}///:~