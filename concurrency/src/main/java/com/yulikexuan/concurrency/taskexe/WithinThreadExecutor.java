//: com.yulikexuan.concurrency.taskexe.WithinThreadExecutor.java

package com.yulikexuan.concurrency.taskexe;


import java.util.concurrent.Executor;


public class WithinThreadExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }

}///:~