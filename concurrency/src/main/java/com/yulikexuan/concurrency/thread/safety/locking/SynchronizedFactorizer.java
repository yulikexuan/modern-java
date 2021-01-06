//: com.yulikexuan.concurrency.thread.safety.locking.SynchronizedFactorizer.java

package com.yulikexuan.concurrency.thread.safety.locking;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.math.BigInteger;

/**
 * SynchronizedFactorizer
 *
 * Servlet that caches last result, but with unnacceptably poor concurrency
 * Don’t do this.
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SynchronizedFactorizer {

    @GuardedBy("this")
    private BigInteger lastNumber;

    @GuardedBy("this")
    private BigInteger[] lastFactors;

    public synchronized void service(ServletRequest req, ServletResponse resp) {

        BigInteger i = extractFromRequest(req);

        if (i.equals(lastNumber)) {
            encodeIntoResponse(resp, lastFactors);
        } else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[] { i };
    }

}///:~