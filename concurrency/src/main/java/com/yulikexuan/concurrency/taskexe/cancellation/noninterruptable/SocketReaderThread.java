//: com.yulikexuan.concurrency.taskexe.cancellation.noninterruptible.SocketReaderThread.java

package com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


/**
 * SocketReaderThread
 * <p/>
 * Encapsulating nonstandard cancellation in a Thread by overriding interrupt
 *
 * Shows a technique for encapsulating nonstandard cancellation
 *
 * ReaderThread manages a single socket connection, reading synchronously from
 * the socket and passing any data received to processBuffer
 *
 * To facilitate terminating a user connection or shutting down the server,
 * Reader-Thread overrides interrupt to both deliver a standard interrupt and
 * close the underlying socket;
 *
 * Thus interrupting a ReaderThread makes it stop what it is doing whether it is
 * blocked in read or in an interruptible blocking method
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
public class SocketReaderThread extends Thread {

    private static final int BUFSZ = 512;

    private final Socket socket;
    private final InputStream in;

    private volatile boolean inputStreamClosed = false;

    private SocketReaderThread(Socket socket, InputStream in) throws IOException {
        this.socket = socket;
        this.in = in;
    }

    public static SocketReaderThread of(@NonNull Socket socket) throws IOException {
        return new SocketReaderThread(socket, socket.getInputStream());
    }

    @Override
    public void interrupt() {
        try (this.in) {
            socket.close();
        } catch (IOException ignored) {
            log.error(">>>>>>> Caught IOException when closing socket {} ",
                    ignored.getMessage());
        } finally {
            super.interrupt();
            log.warn(">>>>>>> The SocketReaderThread was stopped.");
        }
        inputStreamClosed = true;
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[BUFSZ];
            while (true) {
                int count = in.read(buf);
                if (count < 0) {
                    break;
                } else if (count > 0) {
                    processBuffer(buf, count);
                }
            }
        } catch (IOException e) { /* Allow thread to exit */
            // SocketException
            log.error(">>>>>>> Caught {} when reading the Socket.",
                    e.getClass().getSimpleName());
        } finally {
            log.info(">>>>>>> Stopped reading.");
        }
    }

    public void processBuffer(byte[] buf, int count) {
        log.info(">>>>>>> Processing {} bytes buffer", count);
    }

    public boolean isInputStreamClosed() {
        return this.inputStreamClosed;
    }

}///:~