//: com.yulikexuan.jdk.ocp2.enums.TestDays.java

package com.yulikexuan.jdk.ocp2.enums;


import lombok.extern.slf4j.Slf4j;


@Slf4j
class TestDays {

    enum Days {MON, TUE, WED}

    public static void main(String... args) {
        for (Days d : Days.values());
        Days [] d2 = Days.values();
        log.info(">>>>>>> d2[2] is {}", d2[2]);
    }

}///:~