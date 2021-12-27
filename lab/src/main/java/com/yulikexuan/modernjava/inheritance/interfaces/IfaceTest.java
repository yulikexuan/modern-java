//: com.yulikexuan.modernjava.inheritance.interfaces.IfaceTest.java

package com.yulikexuan.modernjava.inheritance.interfaces;


import lombok.extern.slf4j.Slf4j;


interface MyInterface {
    default int doStuff() {
        return 42;
    }
}


@Slf4j
public class IfaceTest implements MyInterface {

    public static void main(String[] args) {
        new IfaceTest().go();
    }

    void go() {
        // INSERT CODE HERE
        log.info(">>>>>>> this iface: {}", doStuff());
        log.info(">>>>>>> super iface: {}", MyInterface.super.doStuff());
    }

    public int doStuff() {
        return 43;
    }

}///:~