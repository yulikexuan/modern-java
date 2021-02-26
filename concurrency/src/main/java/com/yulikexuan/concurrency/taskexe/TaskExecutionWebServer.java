//: com.yulikexuan.concurrency.taskexe.TaskExecutionWebServer.java

package com.yulikexuan.concurrency.taskexe;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;


/**
 * TaskExecutionWebServer
 * <p/>
 * Web server using a thread pool
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class TaskExecutionWebServer {

    private final Executor executor;

    void service() throws IOException {

        ServerSocket socket = new ServerSocket(80);

        while (true) {
            final Socket connection = socket.accept();
            executor.execute(() -> handleRequest(connection));
        }
    }

    /*
     * Must be thread safe
     */
    private static void handleRequest(Socket connection) {
        log.info(">>>>>>> Handling request at Port {}", connection.getPort());
    }

}///:~