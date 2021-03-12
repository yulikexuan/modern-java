//: com.yulikexuan.concurrency.async.IUserServices.java

package com.yulikexuan.concurrency.async;


import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public interface IUserServices {

    CompletableFuture<User> getUsersDetail(UUID userId);
    CompletableFuture<UserCreditRating> getCreditRating(User user);
    CompletableFuture<TaxInfo> getTaxInfo(UUID userId);
}

final class UserServices implements IUserServices {

    private final ExecutorService executor;

    private UserServices(@NonNull ExecutorService executor) {
        this.executor = executor;
    }

    public static UserServices of(@NonNull ExecutorService executor) {
        return new UserServices(executor);
    }

    @Override
    public CompletableFuture<User> getUsersDetail(UUID userId) {
        return CompletableFuture.supplyAsync(
                () -> IUserService.getUserDetails(userId),
                this.executor);
    }

    @Override
    public CompletableFuture<UserCreditRating> getCreditRating(User user) {
        return CompletableFuture.supplyAsync(
                () -> IUserCreditRatingService.getUserCreditRating(user),
                this.executor);
    }

    @Override
    public CompletableFuture<TaxInfo> getTaxInfo(UUID userId) {
        return CompletableFuture.supplyAsync(
                () -> ITaxInfoService.getTaxInfo(userId),
                this.executor);
    }
}

@Value
class User {
    UUID id;
}

@Value
class UserCreditRating {
    User user;
    int rating;
}

@Value
class TaxInfo {
    UUID id;
    String taxInfo;
}

@Value
class TaxReport {
    User user;
    TaxInfo taxInfo;
    private TaxReport(User user, TaxInfo taxInfo) {
        this.user = user;
        this.taxInfo = taxInfo;
    }
    public static TaxReport of(User user, TaxInfo taxInfo) {
        return new TaxReport(user, taxInfo);
    }
}

class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}

// The services below cannot be accessed directly

interface IUserService {

    static User getUserDetails(@NonNull UUID id) {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new NotFoundException(e);
        }

        return new User(id);
    }
}

interface IUserCreditRatingService {

    static UserCreditRating getUserCreditRating(@NonNull User user) {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new NotFoundException(e);
        }

        return new UserCreditRating(user, ThreadLocalRandom.current()
                .nextInt(0, 100));
    }

}

interface ITaxInfoService {

    static TaxInfo getTaxInfo(@NonNull UUID id) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new NotFoundException(e);
        }

        return new TaxInfo(id, RandomStringUtils.randomAlphanumeric(17));
    }

}

///:~