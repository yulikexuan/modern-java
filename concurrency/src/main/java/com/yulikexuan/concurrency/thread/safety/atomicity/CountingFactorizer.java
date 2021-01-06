//: com.yulikexuan.concurrency.thread.safety.atomicity.CountingFactorizer.java

package com.yulikexuan.concurrency.thread.safety.atomicity;


import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.math.BigInteger;
import java.util.concurrent.atomic.LongAdder;


/**
 * CountingFactorizer
 * Servlet that counts requests using LongAdder
 *
 * Because the state of the servlet is the state of the counter and
 * the counter is thread-safe, this servlet is once again thread-safe
 *
 */
@ThreadSafe
public class CountingFactorizer extends GenericServlet implements Servlet {

    private final LongAdder count = new LongAdder();

    public long getCount() {
        return count.longValue();
    }

    public void service (ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger [] factors = factor(i);
        count.increment();
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse res, BigInteger [] factors) {}

    BigInteger extractFromRequest(ServletRequest req) { return null; }

    BigInteger [] factor(BigInteger i) { return null; }

}///:~