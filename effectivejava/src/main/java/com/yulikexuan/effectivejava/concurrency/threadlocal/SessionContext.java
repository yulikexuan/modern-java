//: com.yulikexuan.effectivejava.concurrency.threadlocal.SessionContext.java

package com.yulikexuan.effectivejava.concurrency.threadlocal;


import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor(staticName = "of")
public class SessionContext {

    private final String username;

}///:~