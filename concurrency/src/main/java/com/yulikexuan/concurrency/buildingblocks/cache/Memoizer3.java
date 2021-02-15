//: com.yulikexuan.concurrency.buildingblocks.cache.Memoizer3.java

package com.yulikexuan.concurrency.buildingblocks.cache;


import com.yulikexuan.concurrency.buildingblocks.synchronizers.LaunderThrowable;
import lombok.NonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;


public class Memoizer3<A, V> implements IComputable<A, V> {

    private final ConcurrentMap<A, Future<V>> cache;
    private final IComputable<A, V> computor;

    private Memoizer3(@NonNull ConcurrentMap<A, Future<V>> cache,
                     @NonNull IComputable<A, V> computor) {

        this.cache = cache;
        this.computor = computor;
    }

    public static <A, V> Memoizer3 of(@NonNull IComputable<A, V> computor) {
        ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
        return new Memoizer3(cache, computor);
    }

    @Override
    public V compute(final A arg) throws InterruptedException {

        Future<V> future = this.cache.get(arg);

        if (Objects.isNull(future)) {
            Callable<V> eval = () -> this.computor.compute(arg);
            FutureTask<V> ft = new FutureTask<>(eval);
            future = ft;
            this.cache.put(arg, ft);
            ft.run();
        }

        try {
            return future.get();
        } catch (CancellationException e) {
            cache.remove(arg, future);
            return null;
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }

}///:~