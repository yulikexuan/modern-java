//: com.yulikexuan.effectivejava.generics.RecursiveTypeBound.java


package com.yulikexuan.effectivejava.generics;


import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;


class RecursiveTypeBound {

    static <E extends Comparable<E>> Optional<E> max(@NonNull Collection<E> c) {

        if (c.isEmpty()) {
            throw new IllegalArgumentException("The Collection should not be empty.");
        }

        return c.stream().max(E::compareTo);
    }

}///:~