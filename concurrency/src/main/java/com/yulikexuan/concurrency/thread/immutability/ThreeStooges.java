//: com.yulikexuan.concurrency.thread.immutability.ThreeStooges.java

package com.yulikexuan.concurrency.thread.immutability;


import com.google.common.collect.ImmutableSet;

import javax.annotation.concurrent.Immutable;
import java.util.Set;


/**
 * ThreeStooges
 * <p/>
 * Immutable class built out of mutable underlying objects,
 * demonstration of candidate for lock elision
 *
 * @author Brian Goetz and Tim Peierls
 */
@Immutable
public final class ThreeStooges {

    private final Set<String> stooges = Set.of("Moe", "Larry", "Curly");

    public ThreeStooges() {
    }

    public boolean isStooge(String name) {
        return stooges.contains(name);
    }

    public String getStoogeNames() {
        return ImmutableSet.copyOf(this.stooges).toString();
    }

}///:~