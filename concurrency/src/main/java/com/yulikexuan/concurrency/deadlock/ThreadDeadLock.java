//: com.yulikexuan.concurrency.deadlock.ThreadDeadLock.java

package com.yulikexuan.concurrency.deadlock;


import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ThreadDeadLock {

    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    public class LoadFileTask implements Callable<String> {

        private final Path path;

        public LoadFileTask(Path path) {
            this.path = path;
        }

        public String call() throws Exception {
            // Here's where we would actually read the file
            return RandomStringUtils.randomAlphanumeric(17);
        }
    }

    public class RenderPageTask implements Callable<String> {

        public String call() throws Exception {

            Future<String> header, footer;

            header = exec.submit(new LoadFileTask(Paths.get("head.html")));
            footer = exec.submit(new LoadFileTask(Paths.get("footer.html")));

            String page = renderBody();

            // Will deadlock -- task waiting for result of subtask
            return header.get() + page + footer.get();
        }

        private String renderBody() {
            // Here's where we would actually render the page
            return "";
        }
    }

}///:~