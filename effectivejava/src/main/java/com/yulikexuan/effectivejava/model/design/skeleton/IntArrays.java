//: com.yulikexuan.effectivejava.model.design.skeleton.IntArrays.java


package com.yulikexuan.effectivejava.model.design.skeleton;


import java.util.AbstractList;
import java.util.List;
import java.util.Objects;


/*
 * Skeleton: the main structure that supports a building, etc.
 * Skeketal: That exists only in a basic form, as an outline
 */
public class IntArrays {

    static List<Integer> intArrayAsList(final int[] a) {

        Objects.requireNonNull(a);

        return new AbstractList<>() {

            @Override public Integer get(int i) {
                return a[i];  // Autoboxing (Item 6)
            }

            @Override public Integer set(int i, Integer val) {

                int oldVal = a[i];
                a[i] = val;     // Auto-unboxing

                return oldVal;  // Autoboxing
            }

            @Override public int size() {
                return a.length;
            }

        };
    }

}///:~