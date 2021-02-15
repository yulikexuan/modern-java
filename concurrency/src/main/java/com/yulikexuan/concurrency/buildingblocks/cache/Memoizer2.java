//: com.yulikexuan.concurrency.buildingblocks.cache.Memoizer2.java

package com.yulikexuan.concurrency.buildingblocks.cache;


import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RequiredArgsConstructor(staticName = "of")
public class Memoizer2<A, V> implements IComputable<A, V> {

    private final Map<A, V> cache = new ConcurrentHashMap<>();

    private final IComputable<A, V> computor;

    @Override
    public V compute(A arg) throws InterruptedException {

        V result = cache.get(arg);

        if (result == null) {
            result = computor.compute(arg);
            cache.put(arg, result);
        }

        return result;
    }

}///:~