//: com.yulikexuan.jdk.ocp2.io.Talker.java

package com.yulikexuan.jdk.ocp2.io;


import lombok.extern.slf4j.Slf4j;

import java.io.Console;


@Slf4j
class Talker {

    public static void main(String[] args) {
        Console c = System.console();
        String u = c.readLine("%s", "username: ");
        System.out.println("hello " + u);
        // String pw; // Should be char[]
        char[] pw;
        if (c != null && (pw = c.readPassword("%s", "password: ")) != null) {
            log.info(">>>>>>> Check pw ... ...");
        }
    }

}///:~