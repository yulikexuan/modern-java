//: com.yulikexuan.concurrency.threadpool.NamedThreadFactory.java

package com.yulikexuan.concurrency.threadpool;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;


/**
 * MyThreadFactory
 * <p/>
 * Custom thread factory
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
@AllArgsConstructor(staticName = "of")
public class NamedThreadFactory implements ThreadFactory {

    private final String poolName;

    public Thread newThread(Runnable runnable) {
        return NamedThread.of(runnable, poolName);
    }

}///:~