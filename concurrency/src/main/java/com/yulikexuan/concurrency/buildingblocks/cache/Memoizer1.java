//: com.yulikexuan.concurrency.buildingblocks.cache.Memoizer1.java

package com.yulikexuan.concurrency.buildingblocks.cache;


import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;

import javax.annotation.concurrent.GuardedBy;
import java.util.Map;


@RequiredArgsConstructor(staticName = "of")
public class Memoizer1<A, V> implements IComputable<A, V> {

    @GuardedBy("this")
    private final Map<A, V> cache = Maps.newHashMap();

    private final IComputable<A, V> computor;

    @Override
    public synchronized V compute(A arg) throws InterruptedException {

        V result = cache.get(arg);

        if (result == null) {
            result = computor.compute(arg);
            cache.put(arg, result);
        }

        return result;
    }

}///:~