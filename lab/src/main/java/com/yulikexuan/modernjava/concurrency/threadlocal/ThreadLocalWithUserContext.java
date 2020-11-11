//: com.yulikexuan.modernjava.concurrency.threadlocal.ThreadLocalWithUserContext.java


package com.yulikexuan.modernjava.concurrency.threadlocal;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class ThreadLocalWithUserContext implements Runnable {

    private static ThreadLocal<Context> threadLocalUserContext =
            new ThreadLocal<>();

    private final Integer userId;
    private final UserRepository userRepository;

    private String username;

    @Override
    public void run() {
        this.username = userRepository.getUsernameForUserId(userId);
        threadLocalUserContext.set(Context.of(this.username));
        log.debug(">>>>>>> Thread context for given user id {} is {}",
                this.userId, threadLocalUserContext.get());
    }

    public String getUserName() {
        return username;
    }

}///:~