//: com.yulikexuan.concurrency.taskexe.ThreadPerTaskWebServer.java

package com.yulikexuan.concurrency.taskexe;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


@Slf4j
public class ThreadPerTaskWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            new Thread(() -> handleRequest(connection)).start();
        }
    }

    private static void handleRequest(Socket connection) {
        log.info(">>>>>>> Handling request at Port {}", connection.getPort());
    }

}///:~