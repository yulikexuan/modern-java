//: com.yulikexuan.modernjava.patterns.MySingleton.java


package com.yulikexuan.modernjava.patterns;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


@Slf4j
@Getter
public final class MySingleton {

    public static volatile MySingleton mySingleton;

    private MySingleton() {
    }

    public static MySingleton getInstance() {

        if (Objects.isNull(mySingleton)) {
            synchronized (MySingleton.class) {
                if (Objects.isNull(mySingleton)) {
                    mySingleton = new MySingleton();
                }
            }
        }

        return mySingleton;
    }

}///:~