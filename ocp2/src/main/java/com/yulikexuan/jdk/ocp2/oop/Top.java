//: com.yulikexuan.jdk.ocp2.oop.Top.java

package com.yulikexuan.jdk.ocp2.oop;


import lombok.extern.slf4j.Slf4j;


@Slf4j
class Top {
    Top(String s) {
        log.info(">>>>>>> In Top's constructor: B");
    }
}

@Slf4j
class Bottom2 extends Top {

    Bottom2(String s) {
        super(s);
        log.info(">>>>>>> In Bottom2's constructor: D");
    }

    public static void main(String[] args) {
        log.info(">>>>>>> Calling the main method of Bottom2");
        new Bottom2("C");
    }

}

///:~