//: com.yulikexuan.concurrency.taskexe.cancellation.logging.LogWriter.java

package com.yulikexuan.concurrency.taskexe.cancellation.logging;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * LogWriter
 * <p/>
 * Producer-consumer logging service with no shutdown support
 *
 * @author Brian Goetz and Tim Peierls
 */
public class LogWriter {

    static final int CAPACITY = 1000;

    private final LoggerThread logger;
    private final BlockingQueue<String> queue;

    private LogWriter(@NonNull LoggerThread logger,
                      @NonNull BlockingQueue<String> queue) {

        this.queue = queue;
        this.logger = logger;
    }

    public static LogWriter of(@NonNull Writer writer) {

        BlockingQueue<String> queue = new LinkedBlockingDeque<>(CAPACITY);
        LoggerThread logger = LoggerThread.of(queue, writer);

        return new LogWriter(logger, queue);
    }

    public void start() {
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);
    }

}

@Slf4j
class LoggerThread extends Thread {

    private final BlockingQueue<String> queue;
    private final PrintWriter writer;

    private LoggerThread(@NonNull BlockingQueue<String> queue,
                         @NonNull PrintWriter writer) {

        this.queue = queue;
        this.writer = writer;
    }

    public static LoggerThread of(@NonNull BlockingQueue<String> queue,
                                  @NonNull Writer writer) {

        return new LoggerThread(
                new LinkedBlockingDeque<>(LogWriter.CAPACITY),
                new PrintWriter(writer, true));
    }

    public void run() {
        try {
            while (true) {
                writer.println(queue.take());
            }
        } catch (InterruptedException ignored) {
            log.warn(">>>>>>> The log writer was interrupted by {}",
                    ignored.getClass().getSimpleName());
        } finally {
            writer.close();
        }
    }
}

///:~