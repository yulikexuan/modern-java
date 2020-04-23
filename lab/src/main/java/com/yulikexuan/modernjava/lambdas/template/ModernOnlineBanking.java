//: com.yulikexuan.modernjava.lambdas.template.ModernOnlineBanking.java


package com.yulikexuan.modernjava.lambdas.template;


import lombok.AllArgsConstructor;

import java.util.function.Consumer;


@AllArgsConstructor(staticName = "of")
public class ModernOnlineBanking {

    private final ICustomerRepository customerRepository;

    public void processCustomer(long id, Consumer<Customer> customerHappierMaker) {

        // Fetch Customer
        Customer customer = this.customerRepository.getById(id);

        // Make customer happy
        customerHappierMaker.accept(customer);
    }

}///:~