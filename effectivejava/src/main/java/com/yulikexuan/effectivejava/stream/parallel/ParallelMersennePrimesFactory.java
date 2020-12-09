//: com.yulikexuan.effectivejava.stream.parallel.ParallelMersennePrimesFactory.java

package com.yulikexuan.effectivejava.stream.parallel;


import com.google.common.collect.ImmutableList;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import static java.math.BigInteger.*;


/*
 * Do not parallelize stream pipelines indiscriminately
 * The performance consequences may be disastrous
 *
 * As a rule, performance gains from parallelism are best on streams over
 *   - ArrayList
 *   - HashMap
 *   - HashSet
 *   - ConcurrentHashMap
 *   - arrays
 *   - int ranges
 *   - long ranges
 */
public class ParallelMersennePrimesFactory {

    public static final int MAX_LIMIT = 17;
    public static final int CERTAINTY = 50;

    static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
    }

    /*
     * BigInteger::isProbablePrime(50)
     *   - The probability that this BigInteger is composite does not
     *     exceed 2^(-50)
     */
    static List<BigInteger> getMersennePrimes(int limit) {

        if (limit > MAX_LIMIT) {
            throw new IllegalArgumentException("Too Many!!!");
        }

        return primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                .filter(mersenne -> mersenne.isProbablePrime(CERTAINTY))
                .limit(limit)
                .collect(ImmutableList.toImmutableList());
    }

    /*
     * Parallelizing a pipeline is unlikely to increase its performance
     * if the source is from Stream.iterate, or the intermediate operation
     * limit is used
     *
     * Worse, the default parallelization strategy deals with the
     * unpredictability of limit by assuming there’s no harm in processing
     * a few extra elements and discarding any unneeded results
     */
    static List<BigInteger> getMersennePrimesInParallel(int limit) {

        if (limit > MAX_LIMIT) {
            throw new IllegalArgumentException("Too Many!!!");
        }

        return primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                .parallel()
                .filter(mersenne -> mersenne.isProbablePrime(CERTAINTY))
                .limit(limit)
                .collect(ImmutableList.toImmutableList());
    }

}///:~