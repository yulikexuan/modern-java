//: com.yulikexuan.concurrency.thread.immutability.VolatileCachedFactorizer.java

package com.yulikexuan.concurrency.thread.immutability;


import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.math.BigInteger;


public class VolatileCachedFactorizer extends GenericServlet implements Servlet {

    /*
     * When a thread sets the volatile cache field to reference a new
     * OneValueCache, the new cached data becomes immediately visible to
     * other threads
     */
    private volatile OneValueCache cache = OneValueCache.of();

    @Override
    public void service(ServletRequest req, ServletResponse resp) {

        BigInteger i = extractFromRequest(req);

        /*
         * The cache-related operations cannot interfere with each other because
         * OneValueCache is immutable and the cache field is accessed only once
         * in each of the relevant code paths
         *
         * This combination of an immutable holder object for multiple state
         * variables related by an invariant, and a volatile reference used to
         * ensure its timely visibility, allows VolatileCachedFactorizer to be
         * thread-safe even though it does no explicit locking
         */
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = OneValueCache.of(i, factors);
        }
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[]{i};
    }

}///:~