//: com.yulikexuan.modernjava.optionals.OptionalRecipes.java


package com.yulikexuan.modernjava.optionals;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Optional;


/*
 *  Don’t use orElse() for returning a computed value
 */
@Slf4j
final class OptionalRecipes {

    static final int DEFAULT_LENGTH = 30;

    private List<String> db;
    private List<String> cache;

    private int dbCount;
    private int cacheCount;

    private OptionalRecipes() {
        this.db = Lists.newArrayList();
        this.cache = Lists.newArrayList();
    }

    static OptionalRecipes of() {
        OptionalRecipes optionalRecipes = new OptionalRecipes();
        String firstSecret = RandomStringUtils.randomAlphanumeric(DEFAULT_LENGTH);
        optionalRecipes.db.add(firstSecret);
        optionalRecipes.cache.add(firstSecret);
        return optionalRecipes;
    }

    Optional<String> getFromCache(int id) {
        this.cacheCount++;
        return Optional.ofNullable(this.cache.get(id));
    }

    Optional<String> getFromDB(int id) {
        this.dbCount++;
        return Optional.ofNullable(this.db.get(id));
    }

    int getDbCount() {
        return this.dbCount;
    }

    int getCacheCount() {
        return this.cacheCount;
    }

    String findSecret(final int id) {

        /*
         * Optional::orElse(T t) is always be evaluated
         * Use Optional::orElseGet(Supplier<? extends T> supplier)) instead
         */

//        return getFromCache(id).orElse(getFromDB(id).orElseThrow(
//                () -> new IllegalArgumentException("Not found with id" + id)));

        return this.getFromCache(id).orElseGet(
                () -> this.getFromDB(id).orElseThrow(
                        IllegalArgumentException::new));
    }

    Optional<String> findOne(final int id) {
        return this.getFromCache(id).or(() -> Optional.ofNullable(
                RandomStringUtils.randomAlphanumeric(DEFAULT_LENGTH)));
    }

}///:~