//: com.yulikexuan.concurrency.util.TrackingExecutorService.java

package com.yulikexuan.concurrency.util;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;


public class TrackingExecutorService extends AbstractExecutorService
        implements ITrackingExecutorService {

    private final ExecutorService executor;

    // Cancelled by InterruptedException
    private final Set<Runnable> tasksCancelledAtShutdown;

    // Cancelled by calling ICancellableTask::cancel
    private final Set<ICancellableTask<? super Object>> uninterruptableTasks;

    private TrackingExecutorService(
            ExecutorService executor, Set<Runnable> runnables,
            Set<ICancellableTask<? super Object>> uninterruptableTasks) {

        this.executor = executor;
        this.tasksCancelledAtShutdown = runnables;
        this.uninterruptableTasks = uninterruptableTasks;
    }

    public static TrackingExecutorService of(@NonNull ExecutorService executor) {

        return new TrackingExecutorService(executor,
                Collections.synchronizedSet(Sets.newHashSet()),
                Collections.synchronizedSet(Sets.newHashSet()));
    }

    @Override
    public void shutdown() {
        this.executor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        this.uninterruptableTasks.forEach(t -> t.cancel());
        return this.executor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return this.executor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.executor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {

        return this.executor.awaitTermination(timeout, unit);
    }

    @Override
    public List<Runnable> getCancelledTasks() {

        if (!executor.isTerminated()) {
            throw new IllegalStateException(
                    ">>>>>> The executor is not terminated yet.");
        }

        return ImmutableList.copyOf(this.tasksCancelledAtShutdown);
    }

    public List<ICancellableTask<?>> getCancelledUninterruptableTasks() {

        if (!executor.isTerminated()) {
            throw new IllegalStateException(
                    ">>>>>> The executor is not terminated yet.");
        }

        return this.uninterruptableTasks.stream()
                .filter(t -> t.isCancelled())
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public void execute(final Runnable runnable) {
        this.executor.execute(() -> {
            try {
                runnable.run();
            } finally {
                if (isShutdown() && Thread.currentThread().isInterrupted()) {
                    this.tasksCancelledAtShutdown.add(runnable);
                }
            }
        });
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof ICancellableTask) {
            this.uninterruptableTasks.add((ICancellableTask)callable);
            return ((ICancellableTask<T>) callable).newTask();
        } else {
            return super.newTaskFor(callable);
        }
    }

}///:~