//: com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable.CancellableSocketTaskTest.java

package com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable;


import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import org.junit.jupiter.api.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test FutureTask from ThreadPoolExecutor::newTaskFor - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CancellableSocketTaskTest {

    private static final int PORT = 8089;

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ExecutorService executor;

    private CancellableSocketTask socketTask;

    @BeforeAll
    static void beforeAll() throws Exception {
        serverSocket = new ServerSocket(PORT);
        clientSocket = new Socket("127.0.0.1", PORT);
        executor = ExecutorServiceFactory.createSingleTrackingThreadExecutor();
    }

    @AfterAll
    static void afterAll() throws Exception {
        clientSocket.close();
        serverSocket.close();
    }

    @BeforeEach
    void setUp() {
        this.socketTask = CancellableSocketTask.of(clientSocket);
    }

    @Test
    void able_To_Cancel_The_Socket_Task_Running_By_CancellingExecutor()
            throws Exception {

        // Given
        Future<Void> socketTaskFuture = executor.submit(this.socketTask);

        // When
        TimeUnit.MILLISECONDS.sleep(200);
        socketTaskFuture.cancel(true);

        // Then
        assertThat(clientSocket.isClosed()).isTrue();
        assertThat(socketTaskFuture.isCancelled()).isTrue();
    }

}///:~