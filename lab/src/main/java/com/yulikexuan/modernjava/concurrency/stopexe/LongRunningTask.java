//: com.yulikexuan.modernjava.concurrency.stopexe.LongRunningTask.java


package com.yulikexuan.modernjava.concurrency.stopexe;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor(staticName = "of")
final class LongRunningTask implements Runnable {

    static final long SLEEP_TIME = 500;

    @Override
    public void run() {

        log.info(">>>>>>> Start long running task.");
        try {
            while (!Thread.interrupted()) {
                Thread.sleep(SLEEP_TIME);
            }
        } catch (InterruptedException e) {
            log.warn(">>>>>>> The long running thread {} was interrupted. ",
                    Thread.currentThread().getName());
        }

    }

}///:~