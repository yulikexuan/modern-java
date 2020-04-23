//: com.yulikexuan.modernjava.lambdas.factory.BankProductFactoryTest.java


package com.yulikexuan.modernjava.lambdas.factory;


import com.google.common.collect.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;


class BankProductFactoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Given_Product_Name_Then_Create_The_Product_With_Factory() {

        // Given
        String productName = "BOND";

        // When
        BankProduct product = BankProductFactory.createProduct(productName);

        // Then
        assertThat(product).isExactlyInstanceOf(Bond.class);
    }

    @Test
    void test_Given_Product_Name_Then_Create_The_Product_With_Lambdas() {

        // Given
        Map<String, Supplier<BankProduct>> productFactory = Map.of(
                "loan", Loan::new,
                "stock", Stock::new,
                "bond", Bond::new);

        String productName = "STOCK";

        // When
        BankProduct product = productFactory.get(productName.toLowerCase()).get();

        // Then
        assertThat(product).isExactlyInstanceOf(Stock.class);
    }

}///:~