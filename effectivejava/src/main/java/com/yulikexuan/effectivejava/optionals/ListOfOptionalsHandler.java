//: com.yulikexuan.effectivejava.optionals.ListOfOptionalsHandler.java

package com.yulikexuan.effectivejava.optionals;


import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


class ListOfOptionalsHandler {

    static <T> List<T> transform(Collection<Optional<T>> possibilities) {

        if (CollectionUtils.isEmpty(possibilities)) {
            return Collections.EMPTY_LIST;
        }

        /* Optional::stream
         *
         * If a value is present in the Optional, returns a Stream containing
         * only that value, otherwise returns an empty Stream
         *
         * API Note: This method can be used to transform a Stream of optional
         *           elements to a Stream of present value elements
         */
        return possibilities.stream()
                .flatMap(Optional::stream)
                .collect(ImmutableList.toImmutableList());
    }

}///:~