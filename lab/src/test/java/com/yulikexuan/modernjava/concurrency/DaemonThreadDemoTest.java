//: com.yulikexuan.modernjava.concurrency.DaemonThreadDemoTest.java


package com.yulikexuan.modernjava.concurrency;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;


class DaemonThreadDemoTest {

    private DaemonThreadDemo daemonThreadDemo;
    private LongAdder longAdder;

    @BeforeEach
    void setUp() {
        this.longAdder = new LongAdder();
    }

    @Test
    void test_ExitingExecutorService_Do_Not_Make_JVM_Hanging() {

        // Given
        int timeout= 1000;
        this.daemonThreadDemo = DaemonThreadDemo.of(
                timeout, TimeUnit.MILLISECONDS);

        // When
        this.daemonThreadDemo.getExitingExecutorService().schedule(
                () -> {
                        while (true) {
                            Thread.sleep(200);
                            longAdder.increment();
                            System.out.println(">>>>>>> Being alive ... ... ");
                        }
                    },
                10, TimeUnit.MILLISECONDS);
    }


}///:~