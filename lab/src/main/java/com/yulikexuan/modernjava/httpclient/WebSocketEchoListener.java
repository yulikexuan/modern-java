//: com.yulikexuan.modernjava.httpclient.WebSocketEchoListener.java


package com.yulikexuan.modernjava.httpclient;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;


@Slf4j
@NoArgsConstructor(staticName="create")
public class WebSocketEchoListener implements Listener {

    // Partial text messages are accumulated in this list
    private final List<String> textMessages = new ArrayList<>();

    // This CompletableFuture is complete when an error occurs or the WebSocket
    // is closed. See the onError() and onClose() methods
    private CompletableFuture<String> closeStatus = new CompletableFuture<>();

    @Override
    public void onOpen(WebSocket webSocket) {
        log.info("\n<<<<<<< WebSocket was opened & connected ... ...");
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onText(
            WebSocket webSocket, CharSequence data, boolean last) {

        // Request one more message
        webSocket.request(1);

        // Accumulate the message
        textMessages.add(data.toString());

        if (last) {
            // Received the last part of the message
            String completedMessage = this.textMessages.stream()
                    .collect(Collectors.joining(""));
            log.info("\n<<<<<<<Received Completed Message: \n'{}'\n",
                    completedMessage);
            this.textMessages.clear();
        }

        // Return null to indicate that message processing is complete
        return null;
    }

    @Override
    public CompletionStage<?> onBinary(
            WebSocket webSocket, ByteBuffer data, boolean last) {

        // Request one more message
        webSocket.request(1);

        log.info("\n<<<<<<<Received {} Binary Data", last ? "the last" : "");

        return null;
    }

    @Override
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {

        // Request one more message
        webSocket.request(1);

        log.info("\n<<<<<<< Received {} Ping");

        return null;
    }

    @Override
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {

        // Request one more message
        webSocket.request(1);

        // Decode the message
        String data = new String(message.array());
        log.info("\n<<<<<<< Received a Pong: \n'{}'\n", data);

        // Return null to indicate that message processing is complete
        return null;
    }

    @Override
    public CompletionStage<?> onClose(
            WebSocket webSocket, int statusCode, String reason) {

        // Prepare the close description
        String closeDescription = String.format(
                "<<<<<<< Closed with status %s and reason: %s >>>>>>>",
                statusCode, reason);

        // Complete the CompletableFuture to indiate that the WebSocket
        // connection is closed
        this.closeStatus.complete(closeDescription);

        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        // Complete the CompletableFuture exceptionally to indicate that the
        // WebSocket connection is closed with error
        this.closeStatus.completeExceptionally(error);
    }

    public CompletableFuture<String> closeStatus() {
        return this.closeStatus;
    }

}///:~