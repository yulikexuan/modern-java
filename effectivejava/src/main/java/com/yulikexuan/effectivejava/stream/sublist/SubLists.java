//: com.yulikexuan.effectivejava.stream.sublist.SubLists.java

package com.yulikexuan.effectivejava.stream.sublist;


import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/*
 * Two ways to generate a stream of all the sublists of a list (Pages 219-20)
 */
public class SubLists {

    /*
     * Returns a stream of all the sublists of its input list (Page 219)
     *
     * [a, b, c]
     * [a]        [a, b]            [a, b, c]
     * [a]        [a, b], [b]       [a, b, c], [b, c], [c]
     */
    public static <E> Stream<List<E>> of(@NonNull List<E> list) {
        return Stream.concat(
                Stream.of(Collections.emptyList()),
                prefixes(list).flatMap(SubLists::suffixes));
    }

    static <E> Stream<List<E>> prefixes(List<E> list) {
        return IntStream.rangeClosed(1, list.size())
                .mapToObj(end -> list.subList(0, end));
    }

    static <E> Stream<List<E>> suffixes(List<E> list) {
        return IntStream.range(0, list.size())
                .mapToObj(start -> list.subList(start, list.size()));
    }

}///:~