//: com.yulikexuan.modernjava.cache.CustomerService.java


package com.yulikexuan.modernjava.cache;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;


@CacheConfig(cacheNames={"addresses"})
public class CustomerService {

    @Cacheable
    public String getAddress() {
        return RandomStringUtils.randomAlphanumeric(64);
    }

}///:~