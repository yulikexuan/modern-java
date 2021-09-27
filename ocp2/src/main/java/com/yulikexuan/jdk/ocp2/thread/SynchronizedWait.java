//: com.yulikexuan.jdk.ocp2.thread.SynchronizedWait.java

package com.yulikexuan.jdk.ocp2.thread;


import lombok.extern.slf4j.Slf4j;


@Slf4j
class SynchronizedWait {

    public static synchronized void main(String[] args)
            throws InterruptedException {

        Thread t = new Thread();
        t.start();
        log.info(">>>>>>> X");
        t.wait(10000);
        log.info(">>>>>>> Y");
    }

}///:~