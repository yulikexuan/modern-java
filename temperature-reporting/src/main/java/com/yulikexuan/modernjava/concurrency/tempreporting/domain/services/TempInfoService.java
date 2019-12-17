//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.services.TempInfoService.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.services;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.ITempInfo;
import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.TempInfo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;


@Component
public class TempInfoService implements ITempInfoService {

    static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    @Override
    public ITempInfo fetch(final String town) {

        int temp = RANDOM.nextInt(10);

        if ((town == null) || (temp == 0)) {
            throw new RuntimeException("XXX Error! XXX");
        }

        return TempInfo.builder().town(town).temp(temp).build();
    }

}///:~