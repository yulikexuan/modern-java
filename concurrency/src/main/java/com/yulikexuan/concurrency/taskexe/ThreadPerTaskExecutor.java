//: com.yulikexuan.concurrency.taskexe.ThreadPerTaskExecutor.java

package com.yulikexuan.concurrency.taskexe;


import java.util.concurrent.Executor;


public class ThreadPerTaskExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }

}///:~