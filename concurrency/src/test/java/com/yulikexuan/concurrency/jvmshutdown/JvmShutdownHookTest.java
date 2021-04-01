//: com.yulikexuan.concurrency.jvmshutdown.JvmShutdownHookTest.java

package com.yulikexuan.concurrency.jvmshutdown;


import com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable.CancellableSocketTask;
import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import com.yulikexuan.concurrency.util.TrackingExecutorService;
import org.junit.jupiter.api.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Disabled
@DisplayName("Test Jvm　Shutdown　Hook　- ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JvmShutdownHookTest {

    private TrackingExecutorService trackingExecutor;

    private JvmShutdownHook shutdownHook;

    @BeforeEach
    void setUp() throws Exception {
        this.trackingExecutor = ExecutorServiceFactory
                .createTrackingFixedPoolSizeExecutor(1,
                        Duration.ofMillis(200L));
        this.shutdownHook = JvmShutdownHook.of(
                () -> this.trackingExecutor.shutdownNow());
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
    }

    @Test
    void able_To_Shutdown_The_TrackingExecutor_With_JVM_Shutdown_Hook() throws Exception {

        // Given
        int port = 8089;
        String host = "127.0.0.1";
        ServerSocket serverSocket = new ServerSocket(port);
        CancellableSocketTask task = CancellableSocketTask.of(
                new Socket(host, port));
        trackingExecutor.submit(task);

        TimeUnit.MILLISECONDS.sleep(100L);

        // When
        // Let the shutdown hook to shut down the tracking executor

        // Then

    }


}///:~