//: com.yulikexuan.modernjava.lambdas.template.ModernOnlineBankingTest.java


package com.yulikexuan.modernjava.lambdas.template;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ModernOnlineBankingTest {

    @Mock
    private ICustomerRepository customerRepository;

    private long id;
    private Customer customer;
    private ModernOnlineBanking onlineBanking;

    private Random random;

    @BeforeEach
    void setUp() {
        this.random = new Random(System.currentTimeMillis());
        this.id = this.random.longs(1).findFirst().getAsLong();
        this.customer = Customer.of(this.random.nextLong(), "Mike",
                RandomStringUtils.randomAlphanumeric(10), 0,
                true);
        this.onlineBanking = ModernOnlineBanking.of(this.customerRepository);
    }

    @Test
    void test_Given_Customer_When_Process_Then_Giving_More_Bonus() {

        // Given
        given(this.customerRepository.getById(this.id)).willReturn(this.customer);

        // When
        this.onlineBanking.processCustomer(this.id, c -> c.addBonus(100));

        // Then
        assertThat(this.customer.getBonus()).isEqualTo(100);
    }

    @Test
    void test_Given_Customer_When_Process_Then_Giving_E_Statement() {

        // Given
        given(this.customerRepository.getById(this.id)).willReturn(this.customer);

        // When
        this.onlineBanking.processCustomer(this.id,
                c -> c.setSendingPaperStatement(false));

        // Then
        assertThat(this.customer.isSendingPaperStatement()).isFalse();
    }

}///:~