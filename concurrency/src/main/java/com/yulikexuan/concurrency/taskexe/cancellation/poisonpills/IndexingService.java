//: com.yulikexuan.concurrency.taskexe.cancellation.poisonpills.IndexingService.java

package com.yulikexuan.concurrency.taskexe.cancellation.poisonpills;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * IndexingService
 * <p/>
 * Shutdown with poison pill
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
public class IndexingService {

    private static final int CAPACITY = 100;

    private static final File POISON = new File("");

    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();

    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    private IndexingService(@NonNull final File root,
                           @NonNull BlockingQueue<File> queue,
                           @NonNull final FileFilter fileFilter) {

        this.root = root;
        this.queue = queue;
        this.fileFilter = fileFilter;
    }

    public static IndexingService of(@NonNull final File root,
                                     @NonNull final FileFilter filter) {

        BlockingQueue<File> queue = new LinkedBlockingQueue<File>(CAPACITY);
        FileFilter fileFilter = file -> file.isDirectory() || filter.accept(file);
        return new IndexingService(root, queue, fileFilter);
    }

    private boolean alreadyIndexed(File f) {
        return false;
    }

    public void start() {
        producer.start();
        consumer.start();
    }

    public void stop() {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }

    class CrawlerThread extends Thread {

        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                /* fall through */
            } finally {
                while (true) {
                    try {
                        queue.put(POISON);
                        break;
                    } catch (InterruptedException e1) {
                        /* retry */
                    }
                }
            }
        }

        private void crawl(File root) throws InterruptedException {

            File[] entries = root.listFiles(fileFilter);

            if (entries != null) {
                for (File entry : entries) {
                    if (entry.isDirectory())
                        crawl(entry);
                    else if (!alreadyIndexed(entry))
                        queue.put(entry);
                }
            }
        }

    }

    class IndexerThread extends Thread {
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON)
                        break;
                    else
                        indexFile(file);
                }
            } catch (InterruptedException consumed) {
            }
        }

        public void indexFile(File file) {
            /*...*/
        };
    }

}///:~