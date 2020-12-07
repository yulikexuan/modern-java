//: com.yulikexuan.effectivejava.stream.powerset.PowerSetFactory.java

package com.yulikexuan.effectivejava.stream.powerset;


import lombok.NonNull;

import java.util.*;


public class PowerSetFactory {

    /*
     * Returns the power set of an input set as custom collection (Page 218)
     */
    public static final <E> PowerSet<E> of(@NonNull final Set<E> elements) {

        if (elements.size() > 30) {
            throw new IllegalArgumentException(
                    ">>>>>>> Set is too big: " + elements);
        }

        return PowerSet.of(elements);
    }

}///:~