//: com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable.SocketReaderThreadTest.java

package com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable;


import org.junit.jupiter.api.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@DisplayName("Test interrupting non-interruptable threads - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SocketReaderThreadTest {

    private static final int PORT = 8888;

    private static ServerSocket serverSocket;
    private static Socket clientSocket;

    private SocketReaderThread readerThread;

    @BeforeAll
    static void beforeAll() throws Exception {
        serverSocket = new ServerSocket(PORT);
        clientSocket = new Socket("127.0.0.1", PORT);
    }

    @AfterAll
    static void afterAll() throws Exception {
        clientSocket.close();
        serverSocket.close();
    }

    @BeforeEach
    void setUp() throws Exception {
        this.readerThread = SocketReaderThread.of(clientSocket);
    }

    @Test
    void able_To_Cancel_The_Noninterruptable_Socket_Reader_Thread() throws Exception {

        // Given
        this.readerThread.start();

        TimeUnit.MILLISECONDS.sleep(200);

        // When
        this.readerThread.interrupt();

        await().atMost(Duration.ofMillis(500L)).until(
                () -> this.readerThread.isInterrupted());

        // Then
        assertThat(this.readerThread.isInterrupted()).isTrue();
        assertThat(this.readerThread.isInputStreamClosed()).isTrue();
    }

}///:~