//: com.yulikexuan.concurrency.thread.immutability.OneValueCache.java

package com.yulikexuan.concurrency.thread.immutability;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import lombok.NonNull;

import javax.annotation.concurrent.Immutable;
import java.math.BigInteger;
import java.util.List;


/**
 * OneValueCache
 * <p/>
 * Immutable holder for caching a number and its factors
 *
 * Whenever a group of related data items must be acted on atomically,
 * consider creating an immutable holder class for them
 *
 * Race conditions in accessing or updating multiple related variables can be
 * eliminated by using an immutable object to hold all the variables
 *
 * @author Brian Goetz and Tim Peierls
 */
@Immutable
public final class OneValueCache {

    private final BigInteger lastNumber;
    private final List<BigInteger> lastFactors;

    private OneValueCache(BigInteger i, List<BigInteger> factors) {
        this.lastNumber = i;
        this.lastFactors = ImmutableList.copyOf(factors);
    }

    public static OneValueCache of() {
        return new OneValueCache(null, null);
    }

    public static OneValueCache of(@NonNull BigInteger i,
                                   @NonNull BigInteger[] factors) {

        return new OneValueCache(i, List.of(factors));
    }

    public BigInteger[] getFactors(BigInteger i) {
        if (this.lastNumber == null || !this.lastNumber.equals(i)) {
            return null;
        } else {
            return Iterables.toArray(this.lastFactors, BigInteger.class);
        }
    }

}///:~