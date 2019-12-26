//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.services.TempInfoService.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.services;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo;
import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.InvalidTemperatureException;
import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.TempInfo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;


@Component
public class TempInfoService implements ITempInfoService {

    static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    @Override
    public ITempInfo fetch(final String town) {

        int temp = RANDOM.nextInt(23, 50); // -5C ~ 10C
        long interval = RANDOM.nextLong(500, 1500);

        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
        }

        if (temp > 46) { // 8c
            throw new InvalidTemperatureException("Error: Abnormal Temperature!");
        }

        return TempInfo.builder().town(town).temp(temp).build();
    }

}///:~