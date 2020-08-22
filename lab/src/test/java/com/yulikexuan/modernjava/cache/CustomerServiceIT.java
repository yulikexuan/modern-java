//: com.yulikexuan.modernjava.cache.CustomerServiceIT.java


package com.yulikexuan.modernjava.cache;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@Slf4j
class CustomerServiceIT {

    private static ApplicationContext context;

    private static CustomerService customerService;

    @BeforeAll
    static void initializeDiContainer() {
        context = new AnnotationConfigApplicationContext(CachingConfig.class);
        customerService = new CustomerService();
    }

    @Test
    @RepeatedTest(5)
    void test() {
        log.info(">>>>>>> Address: {}", customerService.getAddress());
    }

}///:~