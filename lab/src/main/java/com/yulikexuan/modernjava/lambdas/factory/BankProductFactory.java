//: com.yulikexuan.modernjava.lambdas.factory.BankProductFactory.java


package com.yulikexuan.modernjava.lambdas.factory;


import java.util.Objects;


class Bond extends BankProduct {
}

class Loan extends BankProduct {
}

class Stock extends BankProduct {
}

abstract class BankProduct {
}

public class BankProductFactory {

    public static BankProduct createProduct(String name) {

        final String productName = Objects.isNull(name) ? "" : name.toLowerCase();

        switch (productName) {
            case "loan" : return new Loan();
            case "bond" : return new Bond();
            case "Stock" : return new Stock();
            default : throw new IllegalArgumentException(
                    "Wrong product name exception.");
        }
    }

}///:~