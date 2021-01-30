//: com.yulikexuan.concurrency.design.NumberRange.java

package com.yulikexuan.concurrency.design;


import javax.annotation.concurrent.NotThreadSafe;
import java.util.concurrent.atomic.AtomicInteger;


/*
 * NumberRange uses two AtomicIntegers to manage its state, but imposes an
 * additional constraint—that the first number be less than or equal to the
 * second
 *
 * The underlying AtomicIntegers are thread-safe, the composite class is not
 *
 * Because the underlying state variables lower and upper are not independent,
 * NumberRange cannot simply delegate thread safety to its thread-safe state
 * variables
 */
@NotThreadSafe
public class NumberRange {

    // INVARIANT: lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);

    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {

        // Warning -- UNSAFE check-then-act

        if (i > upper.get()) {
            throw new IllegalArgumentException("can't set lower to " + i + " > upper");
        }
        lower.set(i);
    }

    public void setUpper(int i) {

        // Warning -- UNSAFE check-then-act

        if (i < lower.get()) {
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        }
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }

}///:~