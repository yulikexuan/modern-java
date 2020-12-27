//: com.yulikexuan.effectivejava.concurrency.threadlocal.Session.java

package com.yulikexuan.effectivejava.concurrency.threadlocal;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.Callable;


@Slf4j
@AllArgsConstructor(staticName = "of")
public class Session implements Callable<String> {

    private static ThreadLocal<SessionContext> sessionContextThreadLocal =
            new ThreadLocal<>();

    private UUID userId;

    @Override
    public String call() {
        String username = UserRepository.INSTANCE.getUsernameById(this.userId)
                .orElseThrow(IllegalArgumentException::new);
        SessionContext sessionContext = SessionContext.of(username);
        sessionContextThreadLocal.set(sessionContext);
        return sessionContextThreadLocal.get().getUsername();
    }

}///:~