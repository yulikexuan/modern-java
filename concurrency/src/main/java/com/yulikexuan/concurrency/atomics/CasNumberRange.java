//: com.yulikexuan.concurrency.unblocking.CasNumberRange.java

package com.yulikexuan.concurrency.atomics;


import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;


/**
 * CasNumberRange
 * <p/>
 * Preserving multivariable invariants using CAS
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class CasNumberRange {

    static final String ILLEGAL_LOWER_ARGUMENT_MESSAGE_TEMPLATE =
            "Can't set lower to %d > upper";

    static final String ILLEGAL_UPPER_ARGUMENT_MESSAGE_TEMPLATE =
            "Can't set upper to %d < lower";

    static final String ILLEGAL_ARGUMENT_MESSAGE_TEMPLATE =
            "Can't set lower to %d > upper";

    private final AtomicReference<IntPair> values =
            new AtomicReference<IntPair>(IntPair.of(0, 0));

    public int getLower() {
        return this.values.get().lower;
    }

    public int getUpper() {
        return this.values.get().upper;
    }

    public void setLower(int i) {

        while (true) {

            IntPair oldv = this.values.get();

            if (i > oldv.upper) {
                throw new IllegalArgumentException(String.format(
                        ILLEGAL_LOWER_ARGUMENT_MESSAGE_TEMPLATE, i));
            }

            IntPair newv = IntPair.of(i, oldv.upper);

            if (this.values.compareAndSet(oldv, newv)) {
                return;
            }
        }
    }

    public void setUpper(int i) {

        while (true) {

            IntPair oldv = values.get();

            if (i < oldv.lower) {
                throw new IllegalArgumentException(String.format(
                        ILLEGAL_UPPER_ARGUMENT_MESSAGE_TEMPLATE, i));
            }

            IntPair newv = IntPair.of(oldv.lower, i);

            if (values.compareAndSet(oldv, newv)) {
                return;
            }
        }
    }

    @Immutable
    private static class IntPair {

        // INVARIANT: lower <= upper
        final int lower;
        final int upper;

        private IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        static IntPair of(int lower, int upper) {
            return new IntPair(lower, upper);
        }

    }

}///:~