//: com.yulikexuan.effectivejava.stream.powerset.PowerSet.java

package com.yulikexuan.effectivejava.stream.powerset;


import com.google.common.collect.Lists;
import lombok.NonNull;

import java.util.AbstractList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PowerSet<E> extends AbstractList<Set<E>> {

    private List<E> source;

    private PowerSet(@NonNull Set<E> elements) {
        this.source = Lists.newArrayList(elements);
    }

    static <E> PowerSet<E> of(@NonNull Set<E> elements) {
        return new PowerSet(elements);
    }

    @Override
    public int size() {
        return 1 << this.source.size(); // 2 to the power srcSize
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Set && this.source.containsAll((Set)o);
    }

    /*
     *    0(a)     1(b)     2(c)     Index        BIN
     *    0        0        0        0            000
     *    1        0        0        1            001
     *    0        1        0        2            010
     *    0        1        1        3            011
     *    1        0        0        4            100
     *    1        0        1        5            101
     *    1        1        0        6            110
     *    1        1        1        7            111
     */
    @Override
    public Set<E> get(int index) {

        Set<E> result = new HashSet<>();

        for (int i = 0; index != 0; i++, index >>= 1) {
            if ((index & 1) == 1) {
                result.add(this.source.get(i));
            }
        }

        return result;
    }

}///:~