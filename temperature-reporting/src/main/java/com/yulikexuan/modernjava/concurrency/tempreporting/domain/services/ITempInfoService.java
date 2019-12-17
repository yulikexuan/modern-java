//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.services.ITempInfoService.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.services;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo;

import java.util.concurrent.ThreadLocalRandom;


public interface ITempInfoService {

    ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    ITempInfo fetch(final String town);

}///:~