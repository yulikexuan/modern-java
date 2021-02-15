//: com.yulikexuan.concurrency.buildingblocks.cache.Factorizer.java

package com.yulikexuan.concurrency.buildingblocks.cache;


import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;


public class Factorizer extends GenericServlet implements Servlet {

    private final IComputable<BigInteger, BigInteger[]> c = this::factor;
    private final IComputable<BigInteger, BigInteger[]> cache = Memoizer.of(c);

    @Override
    public void service(ServletRequest req, ServletResponse res) throws
            ServletException, IOException {

        try {
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(res, cache.compute(i));
        } catch (InterruptedException e) {
            encodeError(res, "factorization interrupted");
        }
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    void encodeError(ServletResponse resp, String errorString) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[] { i };
    }

}///:~