//: com.yulikexuan.concurrency.taskexe.SingleThreadWebServer.java

package com.yulikexuan.concurrency.taskexe;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


@Slf4j
public class SingleThreadWebServer {

    public static void main(String[] args) throws IOException {

        ServerSocket socket = new ServerSocket(80);

        while (true) {
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }

    /*
     * Must be thread safe
     */
    private static void handleRequest(Socket connection) {
        log.info(">>>>>>> Handling request at Port {}", connection.getPort());
    }

}///:~