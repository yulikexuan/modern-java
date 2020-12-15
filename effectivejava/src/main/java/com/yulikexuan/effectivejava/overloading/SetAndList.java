//: com.yulikexuan.effectivejava.overloading.SetAndList.java

package com.yulikexuan.effectivejava.overloading;


import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


class SetAndList {

    static ImmutablePair<Set<Integer>, List<Integer>> addThenRemove() {

        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i); // -3, -2, -1, 0, 1, 2
        }

        for (int i = 0; i < 3; i++) {
            set.remove(i);

            /*
             * -2, -1, 0, 1, 2  // removed -3 // list.remove(0)
             * -2, 0, 1, 2      // removed -1 // list.remove(1)
             * -2, 0, 2         // removed 2  // list.remove(2)
             */
            // list.remove(i);

            list.remove((Integer)i);
        }

        return ImmutablePair.of(set, list);
    }

}///:~