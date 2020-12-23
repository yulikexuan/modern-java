//: com.yulikexuan.effectivejava.foreach.SituationsNotUsingForEach.java

package com.yulikexuan.effectivejava.foreach;


import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;


class SituationsNotUsingForEach {

    // Destructive Filtering with Collection::removeIf
    static <T> void removeElements(@NonNull Collection<T> collection,
                                   @NonNull Predicate<? super T> filter) {

        collection.removeIf(filter);
    }

    // Ill-Conceived Destructive Filtering:
    // ConcurrentModificationException will be thrown
    static <T> void removeElementsUsingForEach(
            @NonNull Collection<T> collection,
            @NonNull Predicate<? super T> filter) {

        for (T t : collection) {
            if (filter.test(t)) {
                collection.remove(t);
            }
        }
    }

    static boolean isEven(int i) {
        return (i & 1) == 0;
    }

    static boolean isOdd(int i) {
        return (i & 1) == 1;
    }

}///:~