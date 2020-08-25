//: com.yulikexuan.effectivejava.generics.UnboundedWildcardType.java


package com.yulikexuan.effectivejava.generics;


import lombok.NonNull;

import java.util.Set;


final class UnboundedWildcardType {

    static long countElementsInCommon(@NonNull Set<?> set1, @NonNull Set<?> set2) {
        long count = set1.stream()
                .filter(set2::contains)
                .count();
        return count;
    }

    // Unable to add any new element to unbounded wildcard generic type
    static void addNewElementToUnboundedType(@NonNull Set<?> set, Object item) {
        // set.add(item);
    }

    static Set<?> castToSet(Object o) {
        if (o instanceof Set<?>) {
            Set<?> set = (Set<?>) o;
            return set;
        } else {
            return null;
        }
    }

}///:~