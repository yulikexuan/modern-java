//: com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable.CancellableSocketTask.java

package com.yulikexuan.concurrency.taskexe.cancellation.noninterruptable;


import com.yulikexuan.concurrency.util.ICancellableTask;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.GuardedBy;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;


@Slf4j
public class CancellableSocketTask implements ICancellableTask<Void> {

    static final int BUFSZ = 512;

    private volatile boolean cancelled = false;

    @GuardedBy("this")
    private final Socket socket;

    private CancellableSocketTask(@NonNull Socket socket) {
        this.socket = socket;
    }

    public static CancellableSocketTask of(@NonNull Socket socket) {
        return new CancellableSocketTask(socket);
    }

    @Override
    public Void call() throws Exception {

        try (InputStream in = this.socket.getInputStream()) {
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

        return null;
    }

    private void processBuffer(byte[] buf, int count) {
        log.info(">>>>>>> Processing {} bytes buffer", count);
    }

    @Override
    public synchronized void cancel() {
        log.info(">>>>>>> Being asked for cancelling this task.");
        try {
            if (socket != null) {
                socket.close();
                cancelled = true;
            }
        } catch (IOException ioe) {
            log.warn(">>>>>>> Caught {} when closing the Socket",
                    ioe.getClass().getSimpleName());
        }
    }

    @Override
    public RunnableFuture<Void> newTask() {
        log.info(">>>>>>> Creating a new FutureTask which contains this SocketTask");
        return new FutureTask<>(this) {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    CancellableSocketTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

}///:~