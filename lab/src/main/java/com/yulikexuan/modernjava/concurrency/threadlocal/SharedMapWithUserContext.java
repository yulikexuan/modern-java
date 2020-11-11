//: com.yulikexuan.modernjava.concurrency.threadlocal.SharedMapWithUserContext.java


package com.yulikexuan.modernjava.concurrency.threadlocal;


import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@AllArgsConstructor(staticName = "of")
public class SharedMapWithUserContext implements Runnable {

    public static final Map<Integer, Context> USER_CONTEXT_PER_USERID
            = new ConcurrentHashMap<>();

    private final Integer userId;
    private final UserRepository userRepository;

    @Override
    public void run() {
        String userName = userRepository.getUsernameForUserId(userId);
        USER_CONTEXT_PER_USERID.put(userId, Context.of(userName));
    }

}///:~