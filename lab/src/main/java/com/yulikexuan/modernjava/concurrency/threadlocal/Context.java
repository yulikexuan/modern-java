//: com.yulikexuan.modernjava.concurrency.threadlocal.Context.java


package com.yulikexuan.modernjava.concurrency.threadlocal;


import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor(staticName = "of")
public class Context {

    private final String username;

}///:~