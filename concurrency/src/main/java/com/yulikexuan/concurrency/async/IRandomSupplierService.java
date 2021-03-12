//: com.yulikexuan.concurrency.async.IRandomSupplierService.java

package com.yulikexuan.concurrency.async;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;


public interface IRandomSupplierService<T> {
    CompletableFuture<T> getFutureSupplier(long millis);
    CompletableFuture<T> getFutureSupplier(long millis, String name);
    CompletableFuture<T> getUnstableFutureSupplier(long millis);
    CompletableFuture<T> getBackupFutureSupplier(long millis);
    CompletableFuture<Optional<T>> getGenericFutureSupplier(long milliis);
}


@AllArgsConstructor(staticName = "of")
class RandomAdSupplierService implements IRandomSupplierService<Ad> {

    static final String DEFAULT_LONG_AD = "Defautl Long Ad ... ... ";
    static final String BACK_UP_AD = "Back Up Ad ... ... ";

    static final long SERVICE_TIME_OUT_MILLIS = 2000L;

    private final ExecutorService executor;

    @Override
    public CompletableFuture<Ad> getFutureSupplier(long millis) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return Ad.of(BACK_UP_AD);
        }, executor);
    }

    @Override
    public CompletableFuture<Ad> getFutureSupplier(
            long millis, @NonNull String name) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return Ad.of(name, Duration.ofMillis(millis));
        }, executor);
    }

    @Override
    public CompletableFuture<Ad> getUnstableFutureSupplier(long millis) {
        return CompletableFuture.supplyAsync(() -> {
            if (millis > SERVICE_TIME_OUT_MILLIS) {
                throw new AdNotFoundException();
            }
            return Ad.of("Long Ad");
        }, executor).exceptionally(e -> Ad.of(DEFAULT_LONG_AD));
    }

    @Override
    public CompletableFuture<Ad> getBackupFutureSupplier(long millis) {

        CompletableFuture<Ad> backUpAdFuture = getFutureSupplier(1000L);

        return CompletableFuture.supplyAsync(() -> {
            if (millis > SERVICE_TIME_OUT_MILLIS) {
                throw new AdNotFoundException();
            }
            return Ad.of("Long Ad");
        }, executor).exceptionallyComposeAsync(e -> backUpAdFuture, executor);
    }

    @Override
    public CompletableFuture<Optional<Ad>> getGenericFutureSupplier(
            long millis) {

        return CompletableFuture.supplyAsync(() -> {
            if (millis > SERVICE_TIME_OUT_MILLIS) {
                throw new AdNotFoundException();
            }
            return Ad.of("Long Ad");
        }, executor).handle(exceptionHandler);
    }

    private final BiFunction<Ad, Throwable, Optional<Ad>> exceptionHandler =
            (ad, exception) -> Objects.isNull(exception) ?
                    Optional.ofNullable(ad) : Optional.empty();

}//: End of class RandomAdSupplierService

@Slf4j
@Value
class Ad {
    String name;
    Duration playTime;
    private Ad(@NonNull String name) {
        this.name = name;
        this.playTime = Duration.ofMillis(1000);
    }
    private Ad(@NonNull String name, @NonNull Duration playTime) {
        this.name = name;
        this.playTime = playTime;
    }
    static Ad of(String name) {
        return new Ad(name);
    }
    static Ad of(String name, Duration playTime) {
        return new Ad(name, playTime);
    }
    void play() {
        log.info(">>>>>>> Playing Ad: {} / {} ms", this.name,
                this.getPlayTime().toMillis());
    }
}

class AdNotFoundException extends RuntimeException {
    public AdNotFoundException() {
        super();
    }
}

///:~