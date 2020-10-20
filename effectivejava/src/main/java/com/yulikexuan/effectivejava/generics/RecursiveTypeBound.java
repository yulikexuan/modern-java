//: com.yulikexuan.effectivejava.generics.RecursiveTypeBound.java


package com.yulikexuan.effectivejava.generics;


import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;


class RecursiveTypeBound {

    public static final String NULL_ERROR_MSG =
            "The Collection should not be empty.";

    static <E extends Comparable<E>> Optional<E> max(@NonNull Collection<E> c) {

        if (c.isEmpty()) {
            throw new IllegalArgumentException(NULL_ERROR_MSG);
        }

        return c.stream().max(E::compareTo);
    }

    static <T extends Comparable<? super T>> T maxFree(
            @NonNull Collection<? extends T> collection) {

        if (collection.isEmpty()) {
            throw new IllegalArgumentException(NULL_ERROR_MSG);
        }

        return collection.stream().max(T::compareTo)
                .map(e -> (T) e)
                .orElseThrow();
    }

}///:~