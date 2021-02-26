//: com.yulikexuan.concurrency.taskexe.parallelism.TimeBudget.java

package com.yulikexuan.concurrency.taskexe.parallelism;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.*;

/**
 * QuoteTask
 * <p/>
 * Requesting travel quotes under a time budget
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TimeBudget {

    static final int DEFAULT_THREAD_POOL_SIZE = 16;

    private final ExecutorService executor;

    private TimeBudget(@NonNull ExecutorService executor) {
        this.executor = executor;
    }

    public static TimeBudget of(int threadPoolSize) throws Exception {

        threadPoolSize = ((threadPoolSize <= 0) ||
                (threadPoolSize > DEFAULT_THREAD_POOL_SIZE)) ?
                DEFAULT_THREAD_POOL_SIZE : threadPoolSize;

        return new TimeBudget(ExecutorServiceFactory
                .createFixedPoolSizeExecutor(DEFAULT_THREAD_POOL_SIZE));
    }


    public List<TravelQuote> getRankedTravelQuotes(
            TravelInfo travelInfo,
            Set<TravelCompany> companies,
            Comparator<TravelQuote> ranking,
            long time,
            TimeUnit unit)
            throws InterruptedException {

        List<QuoteTask> tasks = companies.stream()
                .map(company -> QuoteTask.of(company, travelInfo))
                .collect(ImmutableList.toImmutableList());

        List<Future<TravelQuote>> futures = executor.invokeAll(tasks, time, unit);

        int count = tasks.size();
        List<TravelQuote> quotes = new ArrayList<TravelQuote>(count);
        for (int i = 0; i < count; i++) {
            quotes.add(fetchQuote(tasks.get(i), futures.get(i)));
        }

        Collections.sort(quotes, ranking);

        return quotes;
    }

    private static TravelQuote fetchQuote(
            @NonNull QuoteTask task, @NonNull Future<TravelQuote> futureQuote) {

        try {
            return futureQuote.get();
        } catch (InterruptedException | ExecutionException e) {
            return task.getFailureQuote(e.getCause());
        } catch (CancellationException e) {
            return task.getTimeoutQuote(e);
        }
    }

}

class QuoteTask implements Callable<TravelQuote> {

    private final TravelCompany company;
    private final TravelInfo travelInfo;

    private QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    public static QuoteTask of(TravelCompany company, TravelInfo travelInfo) {
        return new QuoteTask(company, travelInfo);
    }

    TravelQuote getFailureQuote(Throwable t) {
        return null;
    }

    TravelQuote getTimeoutQuote(CancellationException e) {
        return null;
    }

    public TravelQuote call() throws Exception {
        return company.solicitQuote(travelInfo);
    }
}

interface TravelCompany {
    TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception;
}

interface TravelQuote {
}

interface TravelInfo {
}

///:~