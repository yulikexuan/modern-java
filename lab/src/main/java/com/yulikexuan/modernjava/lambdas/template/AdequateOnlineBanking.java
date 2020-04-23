//: com.yulikexuan.modernjava.lambdas.template.AdequateOnlineBanking.java


package com.yulikexuan.modernjava.lambdas.template;


abstract class AdequateOnlineBanking {

    final ICustomerRepository customerRepository;

    AdequateOnlineBanking(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Process Template
    public void processCustomer(long id) {

        // Fetch Customer
        Customer customer = this.customerRepository.getById(id);

        // Make customer happy
        this.makeCustomerHappy(customer);
    }

    abstract void makeCustomerHappy(Customer customer);

}///:~