//: com.yulikexuan.effectivejava.generics.Swap.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.Lists;
import lombok.NonNull;

import java.util.List;


public class Swap {

    /*
     * If a type parameter appears only once in a method declaration,
     * replace it with a wildcard, see method "swapFree" below
     *   - If it's an unbounded type parameter, replace it with an unbounded
     *     wildcard '?'
     *   - If it's a bounded type parameter, replace it with a bounded wildcard
     *     <? extends E> or <? super E>
     */
    public static <E> void swap(@NonNull List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    /*
     * It's not possible to put any value except null into List<?>
     * In some cases, the compiler infers the type of a wildcard.
     * For example, a list may be defined as List<?> but, when evaluating an
     * expression, the compiler infers a particular type from the code.
     * This scenario is known as wildcard capture.
     */
    public static void swapFreeIncorrectly(@NonNull List<?> list, int i, int j) {

        list.add(null);

        // list.add(new Object());
        // list.add(Integer.valueOf(1));

        List<Object> anyOne = Lists.newArrayList();
        anyOne.add(new Object());
        anyOne.add(Integer.valueOf(10));

        List<? super Number> anyNumber = Lists.newArrayList();
        anyNumber.add(Integer.valueOf(1));

        List<? super Object> anyObject = Lists.newArrayList();
        anyObject.add(new Object());
        anyObject.add(Integer.valueOf(1));

        // list.set(i, list.set(j, list.get(i)));
    }

    /*
     * It's not possible to put any value except null into List<?>
     */
    public static void swapFree(@NonNull List<?> list, int i, int j) {
        swap(list, i, j);
    }

    public static void swapTruelyFree(@NonNull List<? super Object> list,
                                           int i, int j) {

        list.set(i, list.set(j, list.get(i)));
    }

}///:~