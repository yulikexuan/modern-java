//: com.yulikexuan.concurrency.buildingblocks.cache.Memoizer.java

package com.yulikexuan.concurrency.buildingblocks.cache;


import com.yulikexuan.concurrency.buildingblocks.synchronizers.LaunderThrowable;
import lombok.NonNull;

import java.util.concurrent.*;

/**
 * Memoizer
 * <p/>
 * Final implementation of Memoizer
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Memoizer<A, V> implements IComputable<A, V> {

    private final ConcurrentMap<A, Future<V>> cache;
    private final IComputable<A, V> computor;

    private Memoizer(@NonNull ConcurrentMap<A, Future<V>> cache,
                     @NonNull IComputable<A, V> computor) {

        this.cache = cache;
        this.computor = computor;
    }

    public static <A, V> Memoizer of(@NonNull IComputable<A, V> computor) {
        ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
        return new Memoizer(cache, computor);
    }

    @Override
    public V compute(A arg) throws InterruptedException {

        while (true) {

            Future<V> f = cache.get(arg);

            if (f == null) {

                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws InterruptedException {
                        return computor.compute(arg);
                    }
                };

                FutureTask<V> ft = new FutureTask<V>(eval);

                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }

            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }

    }

}///:~